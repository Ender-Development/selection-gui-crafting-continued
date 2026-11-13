// v2.2.0

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

import org.ajoberstar.reckon.gradle.ReckonExtension
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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("org.ajoberstar.reckon.settings") version "2.0.0"
}

extensions.configure<ReckonExtension> {
    setDefaultInferredScope("patch")
    stages("dev", "final")
    setScopeCalc(calcScopeFromProp()) // -Preckon.scope=minor|major
    setStageCalc(calcStageFromProp()) // -Preckon.stage=final
}

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
