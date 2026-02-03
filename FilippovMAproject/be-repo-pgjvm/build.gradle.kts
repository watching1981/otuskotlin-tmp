plugins {
    id("build-jvm")
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.beCommon)
    api(projects.beRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.db.postgres)
//  implementation(libs.db.hikari)
    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.beRepoTests)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)

}
