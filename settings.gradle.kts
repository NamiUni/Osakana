@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "osakana"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

pluginManagement {
    includeBuild("build-logic")
}

sequenceOf(
    "api",
    "minecraft-paper"
).forEach {
    include("osakana-$it")
    project(":osakana-$it").projectDir = file(it)
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
