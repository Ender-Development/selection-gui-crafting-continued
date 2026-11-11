import java.util.Properties

pluginManagement {
    repositories {
        maven {
            // RetroFuturaGradle
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
            mavenContent {
                includeGroup("com.gtnewhorizons")
                includeGroup("com.gtnewhorizons.retrofuturagradle")
            }
        }
        gradlePluginPortal()
        mavenCentral()
        google()
        mavenLocal()
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val patternSnake = Regex("_([a-z])")
            settings.extra.properties
                .filter { it.value.toString().isNotBlank() && it.key.endsWith("_version") }
                .forEach { (k, v) -> version(k.replace(patternSnake) { it.value.last().uppercase() }, settings.extra.properties[k].toString()) }
        }
    }
}

val pp =
    Properties().apply {
        val f = file("project.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }

val secrets =
    Properties().apply {
        val f = file("secrets.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }

gradle.beforeProject {
    pp.forEach { (k, v) -> project.extra[k.toString()] = v }
    secrets.forEach { (k, v) -> project.extra[k.toString()] = v }
}

rootProject.name = rootProject.projectDir.name
