plugins {
    kotlin("jvm")
}

repositories {
    maven {
        name = "LocalRepo"
        url = uri("${rootProject.projectDir}/../car-other/build/repo")
    }
}

val resourcesFromLib by configurations.creating

dependencies {
    implementation(kotlin("stdlib"))

    resourcesFromLib("com.github.watching1981:dcompose:1.0:resources@zip")

    implementation("com.github.watching1981:be-api-v1-jacson")
    implementation("com.github.watching1981:be-api-v1-mappers")

    implementation("com.github.watching1981:be-stubs")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
    testImplementation(libs.kotlinx.serialization.core)
    testImplementation(libs.kotlinx.serialization.json)

}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn("extractLibResources")
    }
    register<Copy>("extractLibResources") {
        from(zipTree(resourcesFromLib.singleFile))
        into(layout.buildDirectory.dir("dcompose"))
    }
}
