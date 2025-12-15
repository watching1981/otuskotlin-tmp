pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "otuskotlin-tmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
includeBuild("lessons")
includeBuild("FilippovMAproject")
includeBuild("car-libs")
