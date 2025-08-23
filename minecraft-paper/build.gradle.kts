import xyz.jpenilla.resourcefactory.bukkit.Permission.Default
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("osakana.base")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resourceFactory)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.gremlin)
}

val projectVersion: String by project
version = projectVersion

dependencies {
    implementation(projects.osakanaApi)
    implementation(libs.caffeine)
    implementation(libs.fastUuid)
    implementation(libs.doburokuStandard)
    annotationProcessor(libs.doburokuAnnotationProcessor)

    runtimeDownload(libs.guice) {
        exclude("com.google.guava")
    }
    runtimeDownload(libs.configurateHocon) {
        exclude("net.kyori", "option")
    }
    runtimeDownload(libs.mysql)
    runtimeDownload(libs.mariadb)
    runtimeDownload(libs.postgresql)
    runtimeDownload(libs.h2)
    runtimeDownload(libs.hikariCP)
    runtimeDownload(libs.jdbiCore)
    runtimeDownload(libs.jdbiGuice)
    runtimeDownload(libs.jdbiSqlObject)
    runtimeDownload(libs.jdbiPostgres)
    runtimeDownload(libs.flyway)
    runtimeDownload(libs.flywayMysql) {
        isTransitive = false
    }
    runtimeDownload(libs.flywayPostgres) {
        isTransitive = false
    }

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

    compileJava {
        options.compilerArgs.add("-parameters")
    }

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
            modrinth("miniplaceholders", "MU3nkszR")
            hangar("PlaceholderAPI", "2.11.6")
        }
    }

    writeDependencies {
        repos.add("https://repo.maven.apache.org/maven2/")
        repos.add("https://central.sonatype.com/repository/maven-snapshots/")
        repos.add("https://repo.papermc.io/repository/maven-public/")
    }
}
