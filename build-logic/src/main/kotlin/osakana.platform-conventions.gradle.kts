plugins {
    id("osakana.base-conventions")
    id("xyz.jpenilla.gremlin-gradle")
}

decorateVersion()

dependencies {
    runtimeDownload(libs.guice) {
        exclude("com.google.guava")
    }
}

tasks {
    jar {
        archiveFileName.set("${rootProject.name}-$archiveFileName")
        manifest {
            attributes(
                "osakana-version" to project.version,
                "osakana-commit" to lastCommitHash(),
                "osakana-branch" to currentBranch(),
            )
        }
    }
}

tasks.writeDependencies {
    outputFileName = "osakana-dependencies.txt"
    repos.add("https://repo.papermc.io/repository/maven-public/")
    repos.add("https://repo.maven.apache.org/maven2/")
}

gremlin {
    defaultJarRelocatorDependencies = false
    defaultGremlinRuntimeDependency = false
}
