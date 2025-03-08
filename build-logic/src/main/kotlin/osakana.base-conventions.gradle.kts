plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.git")
    id("net.kyori.indra.licenser.spotless")
    checkstyle
}

version = rootProject.version

indra {
    gpl3OnlyLicense()

    javaVersions {
        target(21)
    }

    github(GITHUB_ORGANIZATION, GITHUB_REPO)
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
}

tasks {
    compileJava {
        options.compilerArgs.add("-Xlint:-processing,-classfile")
        options.compilerArgs.add("-parameters")
    }
}

dependencies {
    checkstyle(libs.stylecheck)
}

checkstyle {
    configDirectory = rootDir.resolve(".checkstyle")
}
