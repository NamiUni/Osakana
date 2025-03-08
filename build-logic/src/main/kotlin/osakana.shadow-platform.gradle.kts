plugins {
    id("osakana.platform-conventions")
    id("com.gradleup.shadow")
}

tasks {
    jar {
        archiveClassifier = "unshaded"
    }
    shadowJar {
        archiveClassifier = null as String?
        configureShadowJar()
        mergeServiceFiles()
    }
}
