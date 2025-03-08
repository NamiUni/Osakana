import xyz.jpenilla.resourcefactory.bukkit.Permission.Default
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("osakana.shadow-platform")
    id("xyz.jpenilla.run-paper")
    id("xyz.jpenilla.resource-factory-paper-convention") version libs.versions.resourceFactory
}

dependencies {
    implementation(projects.osakana.common)

    // Server
    compileOnly(libs.paper.api)

    // Commands
    implementation(libs.cloud.paper)
    implementation(libs.cloud.paper.signed)
}

val mainPackage = "$group.osakana.paper"
paperPluginYaml {
    name = "Osakana"
    authors = listOf("Namiu(Unitarou)")
    website = GITHUB_REPO_URL
    apiVersion = "1.21"
    main = "$mainPackage.OsakanaPaperPlugin"
    loader = "$mainPackage.OsakanaPaperLoader"
    bootstrapper = "$mainPackage.OsakanaPaperBootstrap"
    permissions {
        register("osakana.command.reload") {
            description = "Reloads Osakana's config."
            default = Default.OP
        }
    }
    dependencies {
        server("MiniPlaceholders", Load.BEFORE, false)
        server("LuckPerms", Load.BEFORE, false)
    }
}

tasks {
    shadowJar {
        relocateCloud()
    }
    runServer {
        version.set(libs.versions.minecraft)
        systemProperty("log4j.configurationFile", "log4j2.xml")
        downloadPlugins {
            url("https://download.luckperms.net/1568/bukkit/loader/LuckPerms-Bukkit-5.4.151.jar")
//            github("MiniPlaceholders", "MiniPlaceholders", "2.2.4", "MiniPlaceholders-Paper-2.2.4.jar")
            url("https://ci.codemc.io/job/MiniPlaceholders/job/MiniPlaceholders/6/artifact/jar/MiniPlaceholders-Paper-2.3.0-SNAPSHOT.jar")
            github("MiniPlaceholders", "Player-Expansion", "1.2.0", "MiniPlaceholders-Player-Expansion-1.2.0.jar")
            github("MiniPlaceholders", "PlaceholderAPI-Expansion", "1.2.0", "PlaceholderAPI-Expansion-1.2.0.jar")
            hangar("PlaceholderAPI", "2.11.6")
        }
    }
}
