@file:Suppress("UnstableApiUsage")

rootProject.name = "Osakana"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("api")
include("common")
include("paper")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { snapshotsOnly() }
        }
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { snapshotsOnly() }
        }
        // Paper
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { snapshotsOnly() }
        }
//        maven("https://maven.fabricmc.net/")
        maven("https://repo.jpenilla.xyz/snapshots/") {
            mavenContent { snapshotsOnly() }
        }
    }
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}
