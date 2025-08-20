import xyz.jpenilla.resourcefactory.bukkit.Permission.Default
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("osakana.base")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resourceFactory)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.gremlin)
}

dependencies {
    implementation(projects.osakanaApi)
    implementation(libs.caffeine)
    implementation(libs.doburokuStandard)
    annotationProcessor(libs.doburokuAnnotationProcessor)

    runtimeDownload(libs.guice) {
        exclude("com.google.guava")
    }
    runtimeDownload(libs.configurateHocon) {
        exclude("net.kyori", "option")
    }
    runtimeDownload(libs.hikariCP)
    runtimeDownload(libs.jdbiCore)
    runtimeDownload(libs.jdbiGuice)
    runtimeDownload(libs.jdbiSqlObject)
    runtimeDownload(libs.flyway)

    compileOnly(libs.paperApi)
    compileOnly(libs.miniPlaceholders)
}

val mainPackage = "$group.osakana"

paperPluginYaml {
    name = "Osakana"
    author = "Namiu (うにたろう)"
    apiVersion = "1.21"
    main = "$mainPackage.minecraft.paper.OsakanaPaperPlugin"
    loader = "$mainPackage.minecraft.paper.OsakanaPaperLoader"
    bootstrapper = "$mainPackage.minecraft.paper.OsakanaPaperBootstrap"
    permissions {
//        register("osakana.command.") {
//            description = ""
//            default = Default.OP
//        }
    }
    dependencies {
        server("MiniPlaceholders", Load.BEFORE, false)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.runtimeDownload.get())
    }
}

tasks {
    shadowJar {
        mergeServiceFiles()
        archiveBaseName = paperPluginYaml.name
        archiveClassifier = null as String?
        gremlin {
            listOf("xyz.jpenilla.gremlin")
                .forEach {
                    relocate(it, "$mainPackage.libs.$it")
                }
        }
    }

    runServer {
        version.set("1.21.8")
        systemProperty("log4j.configurationFile", "log4j2.xml")
        downloadPlugins {
            modrinth("luckperms", "v5.5.0-bukkit")
            url("https://ci.codemc.io/job/MiniPlaceholders/job/MiniPlaceholders/14/artifact/jar/MiniPlaceholders-Paper-2.3.1-SNAPSHOT.jar")
            github("MiniPlaceholders", "Player-Expansion", "1.2.0", "MiniPlaceholders-Player-Expansion-1.2.0.jar")
            github("MiniPlaceholders", "PlaceholderAPI-Expansion", "1.2.0", "PlaceholderAPI-Expansion-1.2.0.jar")
            hangar("PlaceholderAPI", "2.11.6")
        }
    }

    writeDependencies {
        repos.add("https://repo.maven.apache.org/maven2/")
        repos.add("https://central.sonatype.com/repository/maven-snapshots/")
        repos.add("https://repo.papermc.io/repository/maven-public/")
    }
}
