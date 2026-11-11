// v2.1.0

// =====================================================================================
//  BUILDSCRIPT-MANAGED FILE: DO NOT EDIT
// =====================================================================================
//  This file is part of the buildscript and is overwritten whenever the buildscript is
//  updated (./gradlew updateBuildScript). Any local changes will be lost.
//  Only edit it if you are sure of what you are doing.
//
//  The first line ("//v<version>") is the version header. It is generated from
//  'buildscript_version' in gradle.properties by ./gradlew syncBuildScriptVersion and
//  is used by the update check. Do not change it by hand and keep it on the first line.
//
//  To configure your project, edit the files meant for that instead of this script:
//    - dependencies.properties   add dependencies
//    - tags.properties           constants for the generated reference/tags class
//    - secrets.properties        credentials (never committed)
//    - project.properties        project data, tweak internal properties
//
//  Check the gradle/template/PROPERTIES.md for every property you can override.
//
//  Update tasks:
//    ./gradlew checkBuildScriptUpdate   check if a newer buildscript version exists
//    ./gradlew updateBuildScript        update the buildscript files
// =====================================================================================

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessTask
import com.gtnewhorizons.retrofuturagradle.fg12shadow.org.eclipse.jgit.storage.file.FileRepositoryBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.lang.GroovyShell
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.internal.extensions.core.extra
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.changelog.Changelog
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.compiler
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.taskTriggers
import java.net.URI
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Properties

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version libs.versions.jetbrainsExtensionVersion
    id("com.gtnewhorizons.retrofuturagradle") version libs.versions.retrofuturagradleVersion
    kotlin("jvm") version libs.versions.kotlinVersion
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
    // formatting
    id("com.diffplug.spotless") version libs.versions.spotlessVersion
    id("zone.rong.clearskies") version libs.versions.clearskiesVersion
    // publishing
    id("org.jetbrains.changelog") version libs.versions.jetbrainsChangelogVersion
    id("net.darkhax.curseforgegradle") version libs.versions.curseforgegradleVersion
    id("com.modrinth.minotaur") version libs.versions.minotaurVersion
}

val repoName = "Ender-Development/EnderDevelopmentTemplate"
val updateBranch = "master"
val templateDir = "gradle/template"

data class ModDependency(
    val identifier: String,
    val flag: Boolean,
    val dep: String,
    val availableOnCurseForge: Boolean = true,
    val availableOnModrinth: Boolean = true,
    val optional: Boolean = false,
)

fun isTemplate(): Boolean {
    val repo =
        FileRepositoryBuilder()
            .setGitDir(File(".git"))
            .readEnvironment()
            .findGitDir()
            .build()
    val remoteUrl = repo.config.getString("remote", "github", "url")
    return remoteUrl.contains(repoName)
}

fun getMarkdownChangelog(): String {
    if (!file("CHANGELOG.md").exists()) throw GradleException("CHANGELOG.md does not exist!")
    val log = changelog.renderItem(changelog.get(modVersion).withHeader(false).withEmptySections(false), Changelog.OutputType.MARKDOWN)
    if (log.isEmpty()) throw GradleException("changelog for version $modVersion is empty!")
    return log
}

fun loadProps(filename: String): Properties =
    Properties().apply {
        val file = file(filename)
        if (file.exists()) {
            return@apply file(filename).inputStream().use { load(it) }
        }
        throw GradleException("File $filename doesn't exist!")
    }

fun getProp(
    name: String,
    default: Any? = null,
): String =
    extra.takeIf { it.has(name) }?.get(name)?.toString()
        ?: findProperty(name)?.toString()
        ?: default?.toString()
        ?: throw GradleException("Property $name needs to be specified!")

fun getPropList(
    name: String,
    delimiter: String = " ",
): MutableList<String> =
    try {
        getProp(name).split(delimiter).filter { it.isNotEmpty() }.toMutableList()
    } catch (_: GradleException) {
        mutableListOf()
    }

fun getPropEnv(name: String): String = System.getenv(name) ?: getProp(name, "")

fun evalProp(
    name: String,
    default: Any? = null,
): String = evalPropValue(getProp(name, default))

fun evalPropValue(value: String): String {
    fun placeHolder(value: String): String {
        var result = value
        val template = "\\$\\{([^}]+)}".toRegex()
        template.findAll(value).forEach {
            val placeholder = it.value
            val key = it.groupValues[1]
            val replacement = if (key == "mod_version") modVersion else evalProp(key)
            result = result.replace(placeholder, replacement)
        }
        return result
    }

    fun shell(value: String): String {
        if (value.startsWith($$"${{") && value.endsWith("}}")) {
            val expression = placeHolder((value.substring(3, value.length - 2).trim()))
            try {
                return GroovyShell().evaluate(expression).toString()
            } catch (e: Exception) {
                throw GradleException("Failed to evaluate expression '$expression' in property evaluation.", e)
            }
        }
        return placeHolder(value)
    }
    return shell(value)
}

val isTemplateRepo = isTemplate()
val versionGradle = getProp("gradle_version")
val kotlinVersion = getProp("kotlin_version")
val useKotlin = getProp("use_kotlin", false).toBoolean()
val editorconfig = getProp("editorconfig", "spotless")
val minecraftVersion = getProp("minecraft_version", "1.12.2")
val rootPackage = getProp("root_package", "org.ender_development")
val userName = getPropEnv("DEV_USERNAME").ifBlank { getProp("minecraft_username", "Developer") }

val modId = getProp("mod_id").lowercase().trim().replace(" ", "_")
val modName = getProp("mod_name")
val modVersion = project.version.toString()

val modDescription = getProp("mod_description", "")
val modAuthors = getPropList("mod_authors", ",").joinToString(", ")
val modCredits = getProp("mod_credits", "Ender-Development.org")
val modUrl = getProp("mod_url")
val modLogoPath = evalProp("mod_logo_path", $$"assets/${mod_id}/textures/logo.png")

val useAccessTransformer = getProp("use_access_transformer", false).toBoolean()
val accessTransformerLocation = evalProp("access_transformer_location", $$"${mod_id}_at.cfg")

val useTags = getProp("use_tags", true).toBoolean() && file("tags.properties").exists()
val tagClassName = evalProp("tag_class_name", $$"${root_package}.${mod_id}.Tags")

val isCoreMod = getProp("is_coremod", false).toBoolean()

val useAssetMover = getProp("use_assetmover", false).toBoolean()
val useBetterConfig = getProp("use_betterconfig", false).toBoolean()
val useCatalyx = getProp("use_catalyx", false).toBoolean() && evalPropValue($$"${{ \"${mod_id}\" != \"catalyx\" }}").toBoolean()
val useConfigAnytime = getProp("use_configanytime", false).toBoolean() || useCatalyx
val useCraftTweaker = getProp("use_crafttweaker", false).toBoolean()
val useForgelinContinuous = getProp("use_forgelincontinuous", false).toBoolean() || useKotlin || useCatalyx
val useGroovyScript = getProp("use_groovyscript", false).toBoolean()
val useModularUi = getProp("use_modularui", false).toBoolean()
val useReachFix = getProp("use_reachfix", false).toBoolean()
val useExpandedEvents = getProp("use_expandedevents", false).toBoolean()

val useMixins = evalProp("use_mixins", false).toBoolean()
val mixinRefMap = evalProp("mixin_refmap", $$"mixins.${mod_id}.refmap.json")
val mixinConnectorClass = evalProp("mixin_connector_class", $$"${root_package}.${mod_id}.MixinConnector")

val mavenUrl = getProp("maven_url", "https://maven.ender-development.org")
val releaseType = getProp("release_type", "release")
if (!listOf("release", "beta", "alpha").contains(releaseType)) throw GradleException("'release_type' is none of ['release', 'beta', 'alpha']")
val isServer = getProp("is_server_side", true).toBoolean()
val isClient = getProp("is_client_side", true).toBoolean()
if (!isClient && !isServer) throw GradleException("At least one of 'client_side' and 'server_side' must be true!")
val curseForgeId = getProp("curseforge_id", "")
val publishToCurseForge = getProp("publish_to_curseforge", false).toBoolean() && curseForgeId.isNotBlank()
val modrinthId = getProp("modrinth_id", "")
val publishToModrinth = getProp("publish_to_modrinth", false).toBoolean() && modrinthId.isNotBlank()
val additionalDependencies = getPropList("additional_dependencies", " ")
val modDependencies =
    listOf(
        ModDependency("mixin-booter", useMixins, ""),
        ModDependency("assetmover", useAssetMover, "com.cleanroommc:assetmover:${getProp("assetmover_version")}"),
        ModDependency("configanytime", useConfigAnytime, "com.cleanroommc:configanytime:${getProp("configanytime_version")}"),
        ModDependency("better-config", useBetterConfig, "io.github.meldexun:betterconfig:1.12.2-${getProp("betterconfig_version")}"),
        ModDependency("crafttweaker", useCraftTweaker, "", optional = true),
        ModDependency("groovyscript", useGroovyScript, "com.cleanroommc:groovyscript:${getProp("groovyscript_version")}", optional = true),
        ModDependency("forgelin-continuous", useForgelinContinuous, "T#io.github.chaosunity.forgelin:Forgelin-Continuous:${evalProp("forgelin_continuous_version")}"),
        ModDependency("catalyx", useCatalyx, "C#org.ender_development:catalyx:${getProp("catalyx_version")}"),
        ModDependency("modularui-uf", useModularUi, "D#curse.maven:modularui-uf-1663175:${getProp("modularui_version")}"),
        ModDependency("reach-fix", useReachFix, "D#curse.maven:reach-fix-556777:${getProp("reachfix_version")}"),
        ModDependency("expanded-events", useExpandedEvents, "D#curse.maven:expanded-events-1396395:${getProp("expandedevents_version")}", availableOnModrinth = false),
    )

val useSpotless = getProp("disable_spotless", false).toBoolean()

val rmLicense = getProp("license", "All Rights Reserved")
val rmContent = evalProp("readme", $$"${mod_description}")
val rmPartnership = getProp("partnership", "")

group = rootPackage

base {
    archivesName = modId
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlin_version")}")
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx_serialization_version")}")
    }
}

clearSkies {
    include("${rootPackage.replace(".", "/")}/**")
}

configurations {
    create("mixinAgent") {
        isCanBeConsumed = false
        isCanBeResolved = true
    }
    all { resolutionStrategy.cacheChangingModulesFor(0, "seconds") }
}

dependencies {
    runtimeOnly("com.cleanroommc:strip-latest-forge-requirements:${getProp("strip_latest_forge_requirements_version")}")
    runtimeOnly("com.cleanroommc:osxnarratorblocker:${getProp("osxnarratorblocker_version")}")

    if (useKotlin) runtimeOnly("futbol.rozbrajacz:rozutils:0.4.0") { isTransitive = false }

    runtimeOnly("curse.maven:scalingguis-319656:5150678")

    val annotation = "org.jetbrains:annotations:${getProp("jetbrains_annotation_version")}"
    compileOnly(annotation)
    annotationProcessor(annotation)

    val mixin: String = modUtils.enableMixins("zone.rong:mixinbooter:${getProp("mixin_booter_version")}", mixinRefMap) as String
    implementation(mixin) { isTransitive = false }
    annotationProcessor(mixin)
    add("mixinAgent", mixin) { isTransitive = false }

    implementation("mezz:jei:${getProp("hei_version")}")
    implementation(rfg.deobf("curse.maven:theonesmeagle-977883:${getProp("top_version")}"))

    if (useCraftTweaker) {
        implementation("CraftTweaker2:CraftTweaker2-API:${getProp("crafttweaker_version")}")
        implementation("CraftTweaker2:ZenScript:${getProp("crafttweaker_version")}")
        implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-${getProp("crafttweaker_version")}")
    }

    val modDeps = modDependencies.map { if (it.flag) it.dep else "" } + loadProps("dependencies.properties").values.map { it.toString().trim() }
    modDeps.filter { it.isNotEmpty() }.forEach { raw ->
        if (raw.count { it == '#' } > 1) throw GradleException("Dependency $raw contains more than one delimiter '#'!")
        val flags = raw.substringBefore('#', "").uppercase()
        val notation = raw.substringAfter('#')

        val transitive = 'T' in flags
        val changing = 'C' in flags

        require(!('R' in flags && 'D' in flags)) { "R and D cannot be combined in '$raw'" }
        require(!('D' in flags && ('T' in flags || 'C' in flags))) { "D cannot be combined with T or C in '$raw'" }

        when {
            'R' in flags -> {
                runtimeOnly(notation) {
                    isTransitive = transitive
                    isChanging = changing
                }
            }

            'D' in flags -> {
                implementation(rfg.deobf(notation))
            }

            else -> {
                implementation(notation) {
                    isTransitive = transitive
                    isChanging = changing
                }
            }
        }
    }
}

idea {
    module {
        inheritOutputDirs = true
        isDownloadJavadoc = true
        isDownloadSources = true
    }
    project {
        jdkName = "azul-25"
        languageLevel = IdeaLanguageLevel(8)
        settings {
            taskTriggers { afterSync("afterSync") }
            runConfigurations {
                fun newTask(
                    name: String,
                    task: String,
                ) = add(Gradle("${runConfigurations.size}. $name").apply { setProperty("taskNames", listOf(task)) })

                fun release(
                    name: String,
                    params: String,
                ) = add(
                    Gradle(name).apply {
                        setProperty("taskNames", listOf((if (isTemplateRepo) "pushRelease" else "releaseAndPublish")))
                        setProperty("scriptParameters", params)
                    },
                )
                clear()
                newTask("Run Client", "runClient")
                newTask("Run Server", "runServer")
                newTask("Run Obfuscated Client", "runObfClient")
                newTask("Run Obfuscated Server", "runObfServer")
                newTask("Build Jars", "build")
                newTask("publish to Maven", "publish")
                newTask("Update Buildscripts", "updateBuildScript")
                newTask("Apply Spotless", "spotlessApply")

                if (isTemplateRepo) {
                    newTask("Generate Template Docs", "generatePropertyDocs")
                }

                release("Bump (patch)", "-Preckon.stage=final")
                release("Bump (minor)", "-Preckon.stage=final -Preckon.scope=minor")
                release("Bump (major)", "-Preckon.stage=final -Preckon.scope=major")
            }
            compiler.javac {
                afterEvaluate {
                    javacAdditionalOptions = "-encoding utf8"
                    moduleJavacAdditionalOptions =
                        mapOf(
                            "${project.name}.main" to
                                tasks.compileJava
                                    .get()
                                    .options.compilerArgs
                                    .joinToString(" ") { "\"$it\"" },
                        )
                }
            }
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
        vendor = JvmVendorSpec.AZUL
    }
    if (getProp("generate_source_jar", true).toBoolean()) withSourcesJar()
    if (getProp("generate_javadocs_jar", true).toBoolean()) withJavadocJar()
}

kotlin {
    jvmToolchain(8)
    compilerOptions {
        extraWarnings = true
    }
}

minecraft {
    mcVersion = minecraftVersion
    mcpMappingChannel = getProp("mapping_channel", "stable")
    mcpMappingVersion = getProp("mapping_version", 39)
    username = userName
    useDependencyAccessTransformers = getProp("use_dependency_at_files", true).toBoolean()

    extraRunJvmArguments.add("-ea:$group.$modId")
    val args = mutableListOf<String>()
    args.add("-Dmixin.hotSwap=true")
    args.add("-Dmixin.checks.interfaces=true")
    args.add("-Dmixin.debug.export=true")
    if (isCoreMod) {
        args.add("-Dlegacy.debugClassLoading=true")
        args.add("-Dlegacy.debugClassLoadingFiner=true")
        args.add("-Dlegacy.debugClassLoadingSave=true")
    }
    extraRunJvmArguments.addAll(args)
    extraRunJvmArguments.addAll(getPropList("extra_jvm_args", ";"))

    if (useTags && !useKotlin) {
        val props = loadProps("tags.properties").ifEmpty { return@minecraft }
        injectedTags.set(props.entries.associate { (k, v) -> k.toString() to evalPropValue(v.toString()).ifEmpty { throw GradleException("Empty tag value found for key: $k") } })
    }
}

publishing {
    repositories {
        maven {
            name = evalProp("maven_name", $$"${mod_id}")
            url = uri(mavenUrl)
            credentials {
                username = getPropEnv("MAVEN_USERNAME")
                password = getPropEnv("MAVEN_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = rootPackage
            artifactId = modId
        }
    }
}

repositories {
    mavenCentral()
    // RetroFuturaGradle
    maven {
        name = "GTNH Maven"
        url = uri("https://nexus.gtnewhorizons.com/repository/public/")
    }
    // JitPack is a novel package repository for JVM and Android projects.
    // It builds Git projects on demand and provides you with ready-to-use artifacts (jar, aar).
    // Docs: https://docs.jitpack.io/
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "SpongePowered Maven"
        url = uri("https://repo.spongepowered.org/maven")
    }
    // HEI, MixinBooter, GroovyScript, Forgelin Continuous, ...
    maven {
        name = "CleanroomMC Maven"
        url = uri("https://maven.cleanroommc.com")
    }
    // Our own maven, meow :3
    maven {
        name = "Ender-Development Maven"
        url = uri("https://maven.ender-development.org/")
    }
    // Better implementation of the curseforge maven, which allows adding mods that disable third party downloads
    exclusiveContent {
        forRepository {
            maven {
                name = "CurseMaven"
                url = uri("https://curse.cleanroommc.com")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    // CraftTweaker, ContentTweaker, BWM, JEI, ...
    maven {
        name = "BlameJared's Maven"
        url = uri("https://maven.blamejared.com/")
    }
    // GTCE, GTCEu, AE2uel, EnderIO
    maven {
        name = "GTCEu Maven"
        url = uri("https://maven.gtceu.com")
    }
    // AE2, Mekanism, ProjectE, ComputerCraft, OpenComputers, ...
    maven {
        name = "Thiakil's Maven"
        url = uri("https://maven.thiakil.com/")
    }
    maven {
        name = "AppleCore's Maven"
        url = uri("https://www.ryanliptak.com/maven/")
    }
    // TiCo, Mantle, HungerOverhaul, Natura, IronChest, ...
    maven {
        name = "Mantle's Maven"
        url = uri("https://dvs1.progwml6.com/files/maven")
    }
    // CTM
    maven {
        name = "tterrag's Maven"
        url = uri("https://maven.tterrag.com/")
    }
    // cofh, codechicken, ProjectRed, p455w0rd, brandon3055, ...
    maven {
        name = "covers1624's Maven"
        url = uri("https://maven.covers1624.net/")
    }
    maven {
        name = "modmuss50's Maven"
        url = uri("https://maven.modmuss50.me/")
    }
    maven {
        name = "BuildCraft's Maven"
        url = uri("https://mod-buildcraft.com/maven/")
    }
    // Large collection of mods from various authors, curseforge maven precursor
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev/")
    }
    mavenLocal() // Must be last for caching to work
}

configure<SpotlessExtension> {
    encoding("UTF-8")
    format("misc") {
        target(".gitignore", ".gitattributes")
        replaceRegex("collapse blank lines", "\\n{3,}", "\n\n")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("properties") {
        target("*.properties", "gradle/**/*.properties")
        // key=value -> key = value
        replaceRegex(
            "normalize assignment spacing",
            "(?m)^([A-Za-z0-9_.\\-]+)[ \\t]*=[ \\t]*(.*)$",
            $$"$1 = $2",
        )
        // at most one blank line in a row
        replaceRegex("collapse blank lines", "\\n{3,}", "\n\n")

        trimTrailingWhitespace()
        endWithNewline()
    }
    freshmark {
        fun latestRelease(): String {
            val lastTag = git("tag").split("\n").last()
            val current = modVersion.split("-")
            return when {
                current.size > 1 -> lastTag
                compareVersions(current.first().split(".").map { it.toInt() }, lastTag.split(".").map { it.toInt() }) > 0 -> current.first()
                else -> lastTag
            }
        }
        target(".github/**/*.md", "docs/**/*.md", "gradle/**/*.md", "src/*/resources/**/*.md", "*.md")
        propertiesFile("gradle.properties")
        properties {
            putAll(extra.properties.mapValues { evalPropValue(it.value.toString()) })
            put("repoName", repoName)
            put("mavenUrl", mavenUrl)
            put("LICENSE", rmLicense)
            put("README", rmContent)
            put("SPONSOR", rmPartnership)
            put("VERSION", latestRelease())
        }
    }
    flexmark {
        target(".github/**/*.md", "docs/**/*.md", "gradle/**/*.md", "src/*/resources/**/*.md", "CHANGELOG.md")
        flexmark(getProp("flexmark_version"))
        trimTrailingWhitespace()
        endWithNewline()
    }
    json {
        target("src/*/resources/**/*.json", "src/*/resources/*.info", "src/*/resources/**/*.mcmeta", "examples/**/*.json", "run/groovy/**/*.json")
        gson().indentWithSpaces(2).sortByKeys().version(getProp("gson_version"))
    }
    shell {
        target("gradlew")
    }
    groovy {
        target("examples/**/*.groovy", "run/groovy/**/*.groovy")
        importOrder()
        removeSemicolons()
        greclipse(getProp("greclipse_version")).configProperties(
            "org.eclipse.jdt.core.formatter.tabulation.char=space",
            "org.eclipse.jdt.core.formatter.tabulation.size=2",
            "org.eclipse.jdt.core.formatter.indent_empty_lines=false",
        )
    }
    if (editorconfig != "spotless") return@configure

    fun cSpan(): String {
        val yFrom = git("log", "--max-parents=0", "--format=%as").split("-").first()
        val yLast = git("log", "-1", "--format=%as").split("-").first()
        return if (yFrom == yLast) yFrom else "$yFrom-$yLast"
    }

    val header = $$"/*\n * Copyright (c) $${cSpan()} $${getPropList("mod_authors", ",").first()}\n * SPDX-License-Identifier: $${getProp("license", "LGPL-3.0-only")}\n */\n\n"
    java {
        target("src/main/java/**/*.java")
        importOrder()
        removeUnusedImports()
        forbidWildcardImports()
        shortenFullyQualifiedTypes()
        googleJavaFormat(getProp("google_java_format_version"))
            .reorderImports(true)
            .formatJavadoc(true)
            .maxLineLength(200)
            .aosp()
        formatAnnotations()
        licenseHeader(header)
    }
    kotlin {
        target("src/main/kotlin/**/*.kt")
        ktlint(getProp("ktlint_version")).editorConfigOverride(mapOf("ktlint_standard_package-name" to "disabled"))
        licenseHeader(header)
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(getProp("ktlint_version"))
    }
}

// Manage Access Transformers
if (useAccessTransformer) {
    accessTransformerLocation.forEach {
        val atFile = file("$projectDir/src/main/resources/$it")
        if (atFile.exists()) {
            tasks.deobfuscateMergedJarToSrg
                .get()
                .accessTransformerFiles
                .from(atFile)
            tasks.srgifyBinpatchedJar
                .get()
                .accessTransformerFiles
                .from(atFile)
        } else {
            throw GradleException("Access Transformer file '$it' does not exist!")
        }
    }
}

tasks.register("afterSync") {
    group = "ender-development"
    description = "Task that runs after the template has been synced. Can be used for custom actions."
    dependsOn(
        tasks.injectTags,
        "switchEditorConfig",
        "createReference",
        "createIJScope",
        "checkBuildScriptUpdate",
        "generateMixinJson",
        "checkPropertyDocs",
    )
    doLast { logger.lifecycle("Current Project Version: $modVersion") }
    finalizedBy(tasks.spotlessApply)
}

fun guessIndent(): String {
    val default = "    "
    val editorconfig = file(".editorconfig").takeIf { it.exists() } ?: return default
    // Crude: ignores sections/globs, but good enough (spec: https://spec.editorconfig.org/)
    val settings =
        editorconfig.useLines { lines ->
            lines
                .map { it.trim() }
                .filter { '=' in it }
                .associate { it.substringBefore('=').trim().lowercase() to it.substringAfter('=').trim() }
        }
    val size = settings["indent_size"]?.toIntOrNull() ?: 4
    return when (settings["indent_style"]?.lowercase()) {
        "tab" -> "\t"
        "space" -> " ".repeat(size)
        else -> default
    }
}

listOf("runClient", "runServer").forEach { task ->
    tasks.named<JavaExec>(task).configure {
        if (isCoreMod) {
            jvmArgs("-Dfml.coreMods.load=${getProp("coremod_plugin_class_name")}")
        }
        if (task.contains("Client") && useGroovyScript) {
            listOf(
                getProp("grs_use_examples_folder", true) to "-Dgroovyscript.use_examples_folder=true",
                getProp("grs_run_ls", false) to "-Dgroovyscript.run_ls=true",
                getProp("grs_generate_examples", false) to "-Dgroovyscript.generate_examples=true",
                getProp("grs_generate_wiki", true) to "-Dgroovyscript.generate_wiki=true",
                getProp("grs_generate_and_crash", false) to "-Dgroovyscript.generate_and_crash=true",
                getProp("grs_log_missing_lang_keys", true) to "-Dgroovyscript.log_missing_lang_keys=true",
                getProp("grs_disable_cache", false) to "-Dgroovyscript.disable_cache=true",
            ).filter { it.first.toBoolean() }.map { it.second }.forEach { jvmArgs(it) }
        }
        if (jvmArgs.isNotEmpty()) logger.lifecycle(jvmArgs.toString())
    }
}

tasks.wrapper {
    gradleVersion = versionGradle
}

tasks.compileInjectedTagsKotlin.configure { dependsOn(tasks.injectTags) }
tasks.compilePatchedMcKotlin.configure { dependsOn(tasks.decompressDecompiledSources) }
tasks.compileMcLauncherKotlin.configure { dependsOn(tasks.createMcLauncherFiles) }
tasks.injectTags.configure {
    onlyIf { useTags && tags.get().isNotEmpty() && !useKotlin }
    outputClassName = tagClassName
}
tasks.prepareObfModsFolder.configure { finalizedBy("prioritizeCoremods") }
tasks.spotlessApply.configure {
    onlyIf { useSpotless }
    dependsOn(tasks.clearSkiesApply)
}
tasks.register("prioritizeCoremods") {
    description = "Load MixinBooter and ConfigAnytime before everything else"
    dependsOn(tasks.prepareObfModsFolder)
    doLast {
        val identifier = Regex("(mixinbooter|configanytime)-[0-9]+\\.[0-9]+\\.jar")
        fileTree("run/obfuscated").filter { it.isFile && it.name.matches(identifier) }.forEach { it.renameTo(File(it.parentFile, "!${it.name}")) }
    }
}

tasks.register("createReference") {
    group = "ender-development"
    description = "Generates the Reference.kt file from tags.properties."
    onlyIf { useKotlin && useTags }
    doLast {
        val tagsFile = "tags.properties"
        val tagsPath = tagClassName.removeSuffix(".Tags")
        val objectName = modName.filter(Char::isLetterOrDigit)
        val tags = loadProps(tagsFile)
        val indent = guessIndent()
        val reference =
            buildString {
                appendLine("package $tagsPath")
                appendLine("")
                appendLine("internal typealias Reference = ${objectName}Reference")
                appendLine("")
                appendLine("/**")
                appendLine(" * Auto-generated reference object containing constants from `$tagsFile`.")
                appendLine(" * Don't change this file manually as it will be overwritten.")
                appendLine(" */")
                appendLine("@Suppress(\"UNUSED\")")
                appendLine("object ${objectName}Reference {")
                tags.forEach { (key, value) ->
                    val eval = (if (value is String) evalPropValue(value) else value).toString()
                    val value = if (eval.toDoubleOrNull() != null) eval else "\"${eval.replace("\"", "\\\"")}\""
                    appendLine("${indent}const val $key = $value")
                }
                appendLine("}")
            }
        val outputPath = "src/main/kotlin/${tagsPath.replace(".", "/")}/${objectName}Reference.kt"
        val outputFile = layout.projectDirectory.file(outputPath).asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(reference.replace("\n", System.lineSeparator()))
    }
}

tasks.register("switchEditorConfig") {
    group = "ender-development"
    description = "Switches the .editorconfig file based on the 'editorconfig' property."
    val spotlessEditorConfig = file("$projectDir/gradle/template/.editorconfig.spotless")
    val rozEditorConfig = file("$projectDir/gradle/template/.editorconfig.roz")

    val destination = file("$projectDir/.editorconfig")

    onlyIf { !getProp("disable_editorconfig", false).toBoolean() }

    doFirst {
        val sourceFile =
            when (editorconfig) {
                "spotless" -> spotlessEditorConfig
                "roz" -> rozEditorConfig
                else -> throw IllegalArgumentException("Unknown editorConfigType: $editorconfig")
            }
        if (!sourceFile.exists()) {
            throw GradleException("Source .editorconfig file '${sourceFile.path}' does not exist!")
        }
        Files.copy(sourceFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

tasks.register("createIJScope") {
    group = "ender-development"
    description = "Creates the IntelliJ scope file for the mod sources"

    val outputFile = layout.projectDirectory.file(".idea/scopes/Mod_Source.xml")

    inputs.property("modName", modName)
    outputs.file(outputFile)

    onlyIf { !isTemplateRepo }

    doLast {
        val filtered = modName.filter(Char::isLetterOrDigit)

        val ijScope =
            buildString {
                appendLine("<component name=\"DependencyValidationManager\">")
                appendLine("  <scope name=\"$modName Source\" pattern=\"file[$filtered.main]:*/&amp;&amp;!file[$filtered.main]:resources//*\" />")
                appendLine("</component>")
            }

        val file = outputFile.asFile
        file.parentFile.mkdirs()
        file.writeText(ijScope)
        logger.lifecycle("IntelliJ scope file created at ${file.toPath().normalize()}")
    }
}

tasks.register("generateMixinJson") {
    group = "ender-development"
    description = "Generate missing mixin files"

    fun mixinCfg(name: String) = "src/main/resources/mixins/mixins.$modId.$name.json"
    val missingConfigs = getPropList("mixin_configs").filter { !file(mixinCfg(it)).exists() }
    onlyIf { useMixins && getProp("generate_mixin_json", true).toBoolean() && missingConfigs.isNotEmpty() }
    doLast {
        missingConfigs.forEach {
            val mixinCfg = file(mixinCfg(it))

            val format =
                Json {
                    prettyPrint = true
                    prettyPrintIndent = guessIndent()
                }
            val cfg =
                buildJsonObject {
                    put("package", "$rootPackage.$modId.")
                    put("refmap", mixinRefMap)
                    put("target", "@env(DEFAULT)")
                    put("minVersion", "0.8.5")
                    put("compatibilityLevel", "JAVA_8")
                    putJsonArray("mixins") { }
                    putJsonArray("server") { }
                    putJsonArray("client") { }
                }
            mixinCfg.writeText(format.encodeToString(JsonObject.serializer(), cfg))
        }
    }
}

tasks.withType<ProcessResources> {
    // This will ensure that this task is redone when the versions change
    inputs.property("minecraft_version", minecraftVersion)
    inputs.property("mod_id", modId)
    inputs.property("mod_name", modName)
    inputs.property("mod_version", modVersion)
    inputs.property("mod_description", modDescription)
    inputs.property("mod_authors", modAuthors)
    inputs.property("mod_credits", modCredits)
    inputs.property("mod_url", modUrl)
    inputs.property("mod_logo_path", modLogoPath)
    inputs.property("mixin_refmap", mixinRefMap)

    // Replace various properties
    fun getFiles(
        pattern: String,
        baseDir: File = file("src/main/resources"),
    ) = fileTree(baseDir) { include(pattern) }.files.map { it.relativeTo(baseDir).invariantSeparatorsPath }
    val mixins = getFiles("mixins/mixins.$modId.*.json")
    val lang = getFiles("assets/$modId/lang/*.lang")
    filesMatching(arrayListOf("mcmod.info", "pack.mcmeta") + mixins + lang) {
        expand(
            "minecraft_version" to minecraftVersion,
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_version" to modVersion,
            "mod_description" to modDescription,
            "mod_authors" to modAuthors,
            "mod_credits" to modCredits,
            "mod_url" to modUrl,
            "mod_logo_path" to modLogoPath,
            "mixin_refmap" to mixinRefMap,
        )
    }

    if (useAccessTransformer) {
        // Make sure Access Transformer files are in META-INF folder
        rename("(.+_at.cfg)", "META-INF/$1")
    }
}

tasks.withType<Jar> {
    manifest {
        val attributeMap = mutableMapOf<String, String>()
        if (isCoreMod) {
            attributeMap["FMLCorePlugin"] = getProp("coremod_plugin_class_name")
            if (getProp("coremod_includes_mod").toBoolean()) {
                attributeMap["FMLCorePluginContainsFMLMod"] = "true"
                val currentTask = gradle.startParameter.taskNames
                val validTasks = arrayOf("build", "prepareObfModsFolder", "runObfClient")
                if (currentTask.isNotEmpty() && currentTask[0] in validTasks) attributeMap["ForceLoadAsMod"] = "true"
            }
        }
        if (useAccessTransformer) {
            attributeMap["FMLAT"] = accessTransformerLocation
        }
        if (useMixins && mixinConnectorClass.isNotBlank()) {
            attributeMap["MixinConnector"] = mixinConnectorClass
        }
        attributes(attributeMap)
    }
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

/** Updated only when the remote first-line version is newer than the local one. */
data class VersionedFile(
    val path: String,
    val headerPrefix: String,
    val headerSuffix: String = "",
    val userSectionMarker: String? = null,
)

val versionedFiles =
    listOf(
        VersionedFile("build.gradle.kts", "// v"),
        VersionedFile("gradle.properties", "# v"),
        VersionedFile("README.md", "<!-- v", " -->"),
        VersionedFile(".gitignore", "# v", userSectionMarker = "# ---- USER SECTION"),
        VersionedFile(".gitattributes", "# v", userSectionMarker = "# ---- USER SECTION"),
    )

class PendingUpdate(
    val path: String,
    val content: String,
    val reason: String,
)

fun connect(url: String): URLConnection =
    URI(url).toURL().openConnection().apply {
        connectTimeout = 10_000
        readTimeout = 10_000
    }

fun fetchText(url: String): String = connect(url).getInputStream().bufferedReader().use { it.readText() }

fun fetchRaw(path: String): String = fetchText("https://raw.githubusercontent.com/$repoName/$updateBranch/$path")

fun listRemoteTemplateFiles(): List<String> {
    val json =
        JsonSlurper().parseText(
            fetchText("https://api.github.com/repos/$repoName/git/trees/$updateBranch?recursive=1"),
        ) as Map<*, *>
    return (json["tree"] as List<*>)
        .map { it as Map<*, *> }
        .filter { it["type"] == "blob" && (it["path"] as String).startsWith("$templateDir/") }
        .map { it["path"] as String }
}

fun readVersion(
    text: String,
    prefix: String,
    suffix: String,
): List<Int>? {
    val first = text.lineSequence().firstOrNull() ?: return null
    if (!first.startsWith(prefix)) return null
    return first
        .removePrefix(prefix)
        .removeSuffix(suffix)
        .trim()
        .split('.')
        .map { it.toIntOrNull() ?: return null }
}

fun compareVersions(
    a: List<Int>,
    b: List<Int>,
): Int {
    for (i in 0 until maxOf(a.size, b.size)) {
        val c = a.getOrElse(i) { 0 }.compareTo(b.getOrElse(i) { 0 })
        if (c != 0) return c
    }
    return 0
}

fun normalize(s: String) = s.replace("\r\n", "\n")

fun collectUpdates(): List<PendingUpdate> {
    val updates = mutableListOf<PendingUpdate>()

    versionedFiles.forEach { versionedFile ->
        val remoteText = fetchRaw(versionedFile.path)
        val remoteVersion = readVersion(remoteText, versionedFile.headerPrefix, versionedFile.headerSuffix) ?: return@forEach
        val local = file(versionedFile.path)
        val localVersion = if (local.exists()) readVersion(local.readText(), versionedFile.headerPrefix, versionedFile.headerSuffix) else null
        if (localVersion == null || compareVersions(remoteVersion, localVersion) > 0) {
            val content =
                versionedFile.userSectionMarker
                    ?.let { mergeUserSection(remoteText, if (local.exists()) local.readText() else null, it) }
                    ?: remoteText
            val from = localVersion?.joinToString(".") ?: "none"
            updates += PendingUpdate(versionedFile.path, content, "$from -> ${remoteVersion.joinToString(".")}")
        }
    }

    // Template files are always updated; for the check, "changed" means content differs or file is missing
    for (path in listRemoteTemplateFiles()) {
        val remoteText = fetchRaw(path)
        val local = file(path)
        when {
            !local.exists() -> updates += PendingUpdate(path, remoteText, "new file")
            normalize(local.readText()) != normalize(remoteText) -> updates += PendingUpdate(path, remoteText, "changed")
        }
    }
    return updates
}

fun mergeUserSection(
    remote: String,
    local: String?,
    markerPrefix: String,
): String {
    val remoteLines = remote.lines()
    val remoteMarker = remoteLines.indexOfFirst { it.startsWith(markerPrefix) }
    if (remoteMarker < 0) return remote // upstream has no marker, nothing to preserve

    val managed = remoteLines.subList(0, remoteMarker + 1)
    val localLines = local?.lines().orEmpty()
    val localMarker = localLines.indexOfFirst { it.startsWith(markerPrefix) }

    val userPart =
        if (localMarker >= 0) {
            localLines.subList(localMarker + 1, localLines.size)
        } else {
            val known = remoteLines.map { it.trim() }.toSet()
            listOf("") + localLines.filter { it.isNotBlank() && it.trim() !in known && !it.startsWith("#") }
        }
    return (managed + userPart).joinToString("\n").trimEnd() + "\n"
}

tasks.register("checkBuildScriptUpdate") {
    group = "ender-development"
    description = "Checks whether a newer build script version is available (does not modify anything)"

    onlyIf { !isTemplateRepo }

    doLast {
        val updates = collectUpdates()
        if (updates.isEmpty()) {
            logger.lifecycle("Build script is up to date.")
        } else {
            logger.lifecycle("Build script update available! Run './gradlew updateBuildScript'")
            for (u in updates) logger.lifecycle("  ${u.path} (${u.reason})")
        }
        if (gradle.gradleVersion != versionGradle) {
            logger.lifecycle("updateBuildScript can also update Gradle from ${gradle.gradleVersion} to $versionGradle")
        }
    }
}

tasks.register("updateBuildScript") {
    group = "ender-development"
    description = "Updates build.gradle.kts, gradle.properties and gradle/template/** to the latest version"

    if (gradle.gradleVersion != versionGradle) {
        dependsOn("wrapper")
    }

    doLast {
        val updates = collectUpdates()
        if (updates.isEmpty()) {
            logger.lifecycle("Build script already up to date!")
            return@doLast
        }
        for (u in updates) {
            val target = file(u.path)
            target.parentFile.mkdirs()
            target.writeText(u.content)
            logger.lifecycle("Updated ${u.path} (${u.reason})")
        }
        logger.lifecycle("Build script updated. Please REIMPORT the project or RESTART your IDE!")
    }
}

fun withVersionHeader(
    text: String,
    prefix: String,
    suffix: String,
    version: String,
): String {
    val nl = if ("\r\n" in text) "\r\n" else "\n"
    val lines = text.split(nl)
    val body = if (lines.first().startsWith(prefix)) lines.drop(1) else lines
    return (listOf("$prefix$version$suffix") + body).joinToString(nl)
}

tasks.register("syncBuildScriptVersion") {
    group = "ender-development"
    description = "Writes the buildscript_version property into the version header of the versioned files"

    onlyIf { isTemplateRepo }
    mustRunAfter(tasks.patchChangelog)
    doLast {
        require(Regex("""\d+(\.\d+)*""").matches(modVersion)) {
            "buildscript_version '$modVersion' is not a valid version (expected e.g. 1.2.3)"
        }

        versionedFiles.forEach {
            val target = file(it.path)
            val old = target.readText()
            val new = withVersionHeader(old, it.headerPrefix, it.headerSuffix, modVersion)
            if (new != old) {
                target.writeText(new)
                logger.lifecycle("Set ${it.path} header to ${it.headerPrefix}$modVersion${it.headerSuffix}")
            } else {
                logger.lifecycle("${it.path} already at $modVersion")
            }
        }
    }
}

tasks.register<TaskPublishCurseForge>("curseforge") {
    // [Docs](https://github.com/Darkhax/CurseForgeGradle)
    group = "publishing"
    description = "Upload project to CurseForge"
    dependsOn(tasks.reobfJar, "verifyPushed")
    onlyIf { publishToCurseForge }
    apiToken = getPropEnv("CURSEFORGE_TOKEN")
    debugMode = getProp("curseforge_debug", false).toBoolean()
    if (curseForgeId.isBlank()) return@register
    val uploadFile = upload(curseForgeId, tasks.reobfJar)
    uploadFile.displayName = "$modId-$version"
    uploadFile.releaseType = releaseType
    uploadFile.addJavaVersion("Java 8")
    uploadFile.addModLoader("Forge")
    uploadFile.addGameVersion(minecraftVersion)
    uploadFile.changelog = getMarkdownChangelog()
    uploadFile.changelogType = "markdown"
    if (isServer) uploadFile.addEnvironment("Server")
    if (isClient) uploadFile.addEnvironment("Client")
    uploadFile.addOptional("had-enough-items")
    uploadFile.addOptional("theonesmeagle")
    modDependencies.filter { it.flag && it.availableOnCurseForge }.forEach {
        if (it.optional) {
            uploadFile.addOptional(it.identifier)
        } else {
            uploadFile.addRequirement(it.identifier)
        }
    }
    additionalDependencies.forEach(uploadFile::addRequirement)
}

modrinth {
    // [Docs](https://github.com/modrinth/minotaur)
    token = getPropEnv("MODRINTH_TOKEN")
    if (modrinthId.isBlank() || token.toString().isBlank()) return@modrinth
    projectId = modrinthId
    versionName = "$modName v$modVersion"
    versionNumber = modVersion
    versionType = releaseType
    uploadFile.set(tasks.reobfJar)
    gameVersions = listOf(minecraftVersion)
    loaders = listOf("forge")
    environment =
        when {
            isClient && !isServer -> "client_only"
            !isClient && isServer -> "server_only"
            else -> "client_and_server"
        }
    debugMode = getProp("modrinth_debug", false).toBoolean()
    changelog = getMarkdownChangelog()
    syncBodyFrom = file("README.md").readText()
    dependencies {
        optional.project("had-enough-items")
        optional.project("theonesmeagle")
        // removeSuffix is needed as modularui-uf isn't published on modrinth
        modDependencies.filter { it.flag && it.availableOnModrinth }.forEach {
            val slug = it.identifier.removeSuffix("-uf")
            if (it.optional) {
                optional.project(slug)
            } else {
                required.project(slug)
            }
        }
        additionalDependencies.forEach(required::project)
    }
}
tasks.modrinth {
    dependsOn(tasks.modrinthSyncBody, "verifyPushed")
    onlyIf { publishToModrinth && getProp("modrinth_sync_readme", true).toBoolean() }
}

// Documentation Generation
enum class PropKind { PROP, LIST, ENV, EVAL }

data class PropUsage(
    val name: String,
    val kind: PropKind,
    val arg: String?,
    val file: String,
    val line: Int,
)

data class PropMeta(
    val category: String,
    val description: String,
) {
    val isComplete get() = category.isNotBlank() && description.isNotBlank()
}

val propCallRegex = Regex("""\b(getPropList|getPropEnv|getProp|evalProp)\(\s*"([^"$\\]+)"\s*(?:,\s*(\$*"(?:[^"\\]|\\.)*"|true|false|-?\d+)\s*)?\)""")
val propMetaFile = file("gradle/template/property-docs.json")
val propDocFile = file("gradle/template/PROPERTIES.md")

fun String.unquoted(): String {
    val s = trimStart('$')
    return if (s.length > 2 && s.first() == '"' && s.last() == '"') s.substring(1, s.length - 1) else this
}

fun PropUsage.isRequired() = kind == PropKind.PROP && arg == null

fun scanProps(source: File): List<PropUsage> {
    val text = source.readLines().joinToString("\n") { if (it.trimStart().startsWith("//")) "" else it }
    return propCallRegex
        .findAll(text)
        .map { m ->
            val kind =
                when (m.groupValues[1]) {
                    "getPropList" -> PropKind.LIST
                    "getPropEnv" -> PropKind.ENV
                    "evalProp" -> PropKind.EVAL
                    else -> PropKind.PROP
                }
            PropUsage(
                name = m.groupValues[2],
                kind = kind,
                arg = m.groups[3]?.value,
                file = source.name,
                line = text.substring(0, m.range.first).count { it == '\n' } + 1,
            )
        }.toList()
}

fun loadPropMeta(): Map<String, PropMeta> {
    if (!propMetaFile.exists()) return emptyMap()
    val root = JsonSlurper().parse(propMetaFile) as Map<*, *>
    return root.entries.associate { (key, value) ->
        val m = value as Map<*, *>
        key as String to PropMeta(m["category"] as? String ?: "", m["description"] as? String ?: "")
    }
}

fun savePropMeta(meta: Map<String, PropMeta>) {
    val out =
        meta.toSortedMap().mapValues { (_, m) ->
            linkedMapOf("category" to m.category, "description" to m.description)
        }
    propMetaFile.parentFile.mkdirs()
    propMetaFile.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(out)) + "\n")
}

fun scanAllProps(): Map<String, List<PropUsage>> =
    listOf(file("build.gradle.kts"))
        .filter { it.exists() }
        .flatMap { scanProps(it) }
        .groupBy { it.name }
        .toSortedMap()

fun renderProp(
    name: String,
    usages: List<PropUsage>,
    meta: PropMeta,
): String {
    val isList = usages.any { it.kind == PropKind.LIST }
    val isEval = usages.any { it.kind == PropKind.EVAL }
    val hasEnv = usages.any { it.kind == PropKind.ENV }
    val required = usages.any { it.isRequired() }

    val defaults = usages.filter { it.kind == PropKind.PROP || it.kind == PropKind.EVAL }.mapNotNull { it.arg }.distinct()

    val type =
        when {
            isEval -> {
                "Evaluated String"
            }

            isList -> {
                val delimiters = usages.filter { it.kind == PropKind.LIST }.map { it.arg ?: "\" \"" }.distinct()
                "List (delimiter ${delimiters.joinToString(", ") { "`$it`" }})"
            }

            defaults.isNotEmpty() && defaults.all { it.unquoted().toBooleanStrictOrNull() != null } -> {
                "Boolean"
            }

            defaults.isNotEmpty() && defaults.all { it.unquoted().toIntOrNull() != null } -> {
                "Integer"
            }

            else -> {
                "String"
            }
        }

    val default =
        when {
            isList -> "empty list"
            defaults.isEmpty() -> "none"
            else -> defaults.joinToString(", ") { "`${it.unquoted()}`" }
        }

    return buildString {
        appendLine("### `$name`")
        appendLine()
        appendLine(meta.description.ifBlank { "_No description yet._" })
        appendLine()
        appendLine("- **Type:** $type")
        appendLine("- **Required:** ${if (required) "yes" else "no"}")
        appendLine("- **Default:** $default")
        if (hasEnv) appendLine("- **Environment variable:** `$name` takes precedence over the property")
    }
}

fun renderPropDocs(
    usages: Map<String, List<PropUsage>>,
    meta: Map<String, PropMeta>,
): String {
    val uncategorized = "⚠️ Uncategorized"
    val byCategory = usages.keys.groupBy { meta[it]?.category?.ifBlank { null } ?: uncategorized }
    val categories = byCategory.keys.sortedWith(compareBy({ it == uncategorized }, { it.split(" ", limit = 2).last().lowercase() }))

    return buildString {
        appendLine("<!-- Generated by ./gradlew generatePropertyDocs from gradle/property-docs.json. Do not edit by hand. -->")
        appendLine()
        appendLine("# Properties")
        for (category in categories) {
            appendLine()
            appendLine("## $category")
            for (name in byCategory.getValue(category).sortedWith(compareBy({ !it.startsWith("use_") }, { !it.endsWith("_version") }, { it.lowercase() }))) {
                appendLine()
                append(renderProp(name, usages.getValue(name), meta[name] ?: PropMeta("", "")))
            }
        }
    }
}

tasks.register("checkPropertyDocs") {
    group = "ender-development"
    description = "Logs properties used in the build script that are missing or incomplete in property-docs.json"
    onlyIf { isTemplateRepo }
    doLast {
        val usages = scanAllProps()
        val meta = loadPropMeta()

        val missing = usages.keys - meta.keys
        val incomplete = usages.keys.filter { it in meta && !meta.getValue(it).isComplete }
        val stale = meta.keys - usages.keys

        if (missing.isEmpty() && incomplete.isEmpty() && stale.isEmpty()) {
            logger.lifecycle("All ${usages.size} properties are documented.")
            return@doLast
        }
        if (missing.isNotEmpty()) {
            logger.lifecycle("${missing.size} properties not in ${propMetaFile.name}:")
            missing.forEach { logger.lifecycle("  $it (${usages.getValue(it).first().let { u -> "${u.file}:${u.line}" }})") }
        }
        if (incomplete.isNotEmpty()) {
            logger.lifecycle("${incomplete.size} properties with empty category or description:")
            incomplete.forEach { logger.lifecycle("  $it") }
        }
        if (stale.isNotEmpty()) {
            logger.lifecycle("${stale.size} entries no longer used in the build script:")
            stale.forEach { logger.lifecycle("  $it") }
        }
        logger.lifecycle("Run './gradlew generatePropertyDocs' to add stubs and regenerate PROPERTIES.md.")
    }
}

tasks.register("generatePropertyDocs") {
    group = "ender-development"
    description = "Adds new properties to property-docs.json and regenerates PROPERTIES.md"
    onlyIf { isTemplateRepo }
    doLast {
        val usages = scanAllProps()
        val meta = loadPropMeta().toMutableMap()

        val added = usages.keys.filter { it !in meta }
        added.forEach { meta[it] = PropMeta("", "") }
        savePropMeta(meta)

        propDocFile.writeText(renderPropDocs(usages, meta))

        if (added.isNotEmpty()) {
            logger.lifecycle("Added ${added.size} new entries to ${propMetaFile.name}: ${added.joinToString()}")
            logger.lifecycle("Fill in their category and description, then run this task again.")
        }
        logger.lifecycle("Wrote ${propDocFile.name} (${usages.size} properties).")
    }
}

val changelogGroups = listOf("Changes", "Bugfixes", "Miscellaneous")
val prefixToGroup = mapOf("fix" to "Bugfixes", "misc" to "Miscellaneous")
val changelogFile = file(if (isTemplateRepo) "gradle/template/CHANGELOG.md" else "CHANGELOG.md")

changelog {
    version.set(modVersion)
    versionPrefix.set("")
    groups.set(changelogGroups)
    repositoryUrl.set(modUrl)
    path = changelogFile.canonicalPath
}

fun git(vararg args: String): String {
    val p =
        ProcessBuilder("git", *args)
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()
    val out =
        p.inputStream
            .bufferedReader()
            .readText()
            .trim()
    check(p.waitFor() == 0) { "git ${args.joinToString(" ")} failed:\n$out" }
    return out
}

tasks.register("verifyRelease") {
    group = "release"
    description = "Fails unless tree is clean, HEAD is untagged and the version is final"
    notCompatibleWithConfigurationCache("runs git")
    doLast {
        check(git("status", "--porcelain").isEmpty()) { "Working tree is not clean" }
        check(git("tag", "--points-at", "HEAD").isEmpty()) { "HEAD is already tagged, nothing to release" }
        check('-' !in modVersion) { "Not a release version ($modVersion). Run with -Preckon.stage=final" }
    }
}
tasks.patchChangelog { mustRunAfter("collectCommits") }
tasks.withType<SpotlessTask>().configureEach {
    mustRunAfter("collectCommits", tasks.patchChangelog, "syncBuildScriptVersion")
}
tasks.build { mustRunAfter(tasks.spotlessApply) }
tasks.register("prepareRelease") {
    group = "release"
    description = "Prepares the final changes, updates version references etc."
    dependsOn("verifyRelease", "collectCommits", tasks.patchChangelog, "syncBuildScriptVersion", tasks.spotlessApply, tasks.build)
}
tasks.register("tagRelease") {
    group = "release"
    description = "Builds, patches the changelog, commits and tags locally (nothing is pushed)"
    notCompatibleWithConfigurationCache("runs git")
    dependsOn("prepareRelease")
    doLast {
        val diffLink = changelog.instance.get().links[modVersion]
        val commitMsg =
            buildString {
                appendLine("Release $modVersion")
                appendLine("Diff: $diffLink")
                appendLine(git("log", "-1", "--format='Signed-off-by: %an <%ae>'").removeSurrounding("'"))
            }
        if (isTemplateRepo) {
            versionedFiles.forEach { git("add", file(it.path).canonicalPath) }
        }
        git("add", changelogFile.canonicalPath)
        git("commit", "-m", "Release $commitMsg")
        git("tag", "-a", modVersion, "-m", changelog.renderItem(changelog.get(modVersion), Changelog.OutputType.PLAIN_TEXT))
    }
}
tasks.register("collectCommits") {
    group = "release"
    description = "Adds commits since the last tag to [Unreleased], grouped by prefix"
    notCompatibleWithConfigurationCache("runs git")
    mustRunAfter("verifyRelease")
    doLast {
        // commits since last version tag
        val lastTag = runCatching { git("describe", "--tags", "--abbrev=0", "--match", "[0-9]*") }.getOrNull()
        val range = if (lastTag != null) "$lastTag..HEAD" else "HEAD"
        val subjects = git("log", range, "--no-merges", "--pretty=format:%s").lines().filter { it.isNotBlank() && !it.startsWith("Release ") }

        // classify: "fix: x" -> Bugfixes, "misc: x" -> Miscellaneous, else Changes
        val prefix = Regex("^(fix|misc)\\s*:\\s*(.+)$", RegexOption.IGNORE_CASE)
        val newEntries: List<Pair<String, String>> =
            subjects.map { s ->
                val m = prefix.find(s)
                if (m != null) {
                    prefixToGroup.getValue(m.groupValues[1].lowercase()) to m.groupValues[2].trim()
                } else {
                    "Changes" to s.trim()
                }
            }

        // locate [Unreleased] section
        val lines = changelogFile.readLines()
        val start = lines.indexOfFirst { it.trim().startsWith("## [Unreleased]") }
        require(start >= 0) { "No '## [Unreleased]' section in CHANGELOG.md" }
        val linkRef = Regex("^\\[[^\\]]+\\]:\\s")
        val end = (start + 1 until lines.size).firstOrNull { lines[it].startsWith("## ") || linkRef.containsMatchIn(lines[it]) } ?: lines.size

        // parse existing entries
        val grouped = linkedMapOf<String, MutableList<String>>()
        changelogGroups.forEach { grouped[it] = mutableListOf() }
        var current = "Changes"
        for (line in lines.subList(start + 1, end)) {
            when {
                line.startsWith("### ") -> {
                    current = line.removePrefix("### ").trim()
                    grouped.getOrPut(current) { mutableListOf() }
                }

                line.isBlank() -> {
                    Unit
                }

                line.startsWith("- ") || line.startsWith("* ") -> {
                    grouped.getOrPut(current) { mutableListOf() }.add(line.drop(2).trim())
                }

                else -> {
                    val items = grouped[current]
                    if (!items.isNullOrEmpty()) {
                        items[items.lastIndex] = items.last() + "\n" + line
                    }
                }
            }
        }

        // merge, skipping anything already present
        var added = 0
        for ((group, text) in newEntries) {
            if (grouped.values.none { text in it }) {
                grouped.getOrPut(group) { mutableListOf() }.add(text)
                added++
            }
        }
        if (added == 0) {
            logger.lifecycle("No new changelog entries")
            return@doLast
        }

        // render in fixed group order, empty groups omitted
        val body =
            buildList {
                add("")
                grouped.filterValues { it.isNotEmpty() }.forEach { (g, items) ->
                    add("### $g")
                    items.forEach { add("- $it") }
                    add("")
                }
            }
        val result = lines.subList(0, start + 1) + body + lines.subList(end, lines.size)
        changelogFile.writeText(result.joinToString("\n") + "\n")
        logger.lifecycle("Added $added changelog entries")
    }
}
tasks.withType<PublishToMavenRepository>().configureEach {
    dependsOn("verifyPushed")
    onlyIf { !isTemplateRepo }
}
tasks.register("verifyPushed") {
    group = "release"
    description = "Fails unless the release tag exists on remote and points at HEAD"
    notCompatibleWithConfigurationCache("runs git")
    mustRunAfter("pushRelease")
    doLast {
        check('-' !in modVersion) { "Not a release version ($modVersion), refusing to publish" }
        val head = git("rev-parse", "HEAD")
        git("remote").split("\n").forEach { remote ->
            val remoteShas =
                git("ls-remote", "--tags", remote)
                    .lines()
                    .map { it.split('\t') }
                    .filter { it.size == 2 && (it[1] == "refs/tags/$modVersion^{}" || it[1] == "refs/tags/$modVersion") }
                    .map { it[0] }
            check(head in remoteShas) { "Tag $modVersion is not on $remote at HEAD ($head). Push it first; nothing was published." }
        }
    }
}
tasks.register("pushRelease") {
    group = "release"
    description = "Atomically pushes the release commit and tag"
    notCompatibleWithConfigurationCache("runs git")
    dependsOn("tagRelease")
    doLast {
        git("remote").split("\n").forEach { git("push", "--atomic", it, "HEAD", "refs/tags/$modVersion") }
    }
}
tasks.register("releaseAndPublish") {
    group = "release"
    description = "release, then publish. Run with -Preckon.stage=final"
    dependsOn("pushRelease", tasks.publish)
}
