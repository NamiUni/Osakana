plugins {
    id("osakana.base")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    compileOnlyApi(libs.adventureApi)
}
