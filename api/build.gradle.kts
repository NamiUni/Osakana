plugins {
    id("osakana.base-conventions")
}

description = "API for interfacing with the Osakana Minecraft mod/plugin"

dependencies {
    // Doesn't add any dependencies, only version constraints
    api(platform(libs.adventure.bom))

    // Provided by platform
    compileOnlyApi(libs.adventure.api)
    compileOnlyApi(libs.adventure.minimessage)

    // Provided by Minecraft
    compileOnlyApi(libs.gson)

    compileOnlyApi(libs.jspecify)
}
