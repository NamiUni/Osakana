plugins {
    id("osakana.base-conventions")
}

dependencies {
    api(projects.osakana.api)
    api(libs.gremlin.runtime)

    // Logger
    compileOnlyApi(libs.adventure.logger)

    // Configs
    api(libs.configurate)
    api(libs.adventure.serializer.configurate4)

    // Translations
    api(libs.moonshine)

    // Commands
    api(platform(libs.cloud.bom))
    api(libs.cloud.core)

    // Integrations
    compileOnlyApi(libs.miniplaceholders)

    compileOnlyApi(libs.guice) {
        exclude("com.google.guava")
    }
}
