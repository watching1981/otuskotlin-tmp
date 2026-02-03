plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.spring.mockk)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))



    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Внутренние модели
    implementation(project(":be-common"))
    implementation(project(":be-app-common"))
    implementation("com.github.watching1981.car:car-lib-logging-logback")

    // v1 api
    implementation(project(":be-api-v1-jacson"))
    implementation(project(":be-api-v1-mappers"))

    // v2 api
    implementation(project(":be-api-v2-kmp"))

    // biz
    implementation(project(":be-biz"))

    // DB
    implementation(projects.beRepoStubs)
    implementation(projects.beRepoInmemory)
    implementation(projects.beRepoPgjvm)
    testImplementation(projects.beRepoCommon)
    testImplementation(projects.beStubs)

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.spring.mockk)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1", "spec-v2").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    environment("MKPLADS_DB", "test_db")
}
tasks.bootBuildImage {
    builder = "paketobuildpacks/builder-jammy-base:latest"
    environment.set(mapOf("BP_HEALTH_CHECKER_ENABLED" to "true"))
    buildpacks.set(
        listOf(
            "docker.io/paketobuildpacks/adoptium",
            "urn:cnb:builder:paketo-buildpacks/java",
            "docker.io/paketobuildpacks/health-checker:latest"
        )
    )

}
