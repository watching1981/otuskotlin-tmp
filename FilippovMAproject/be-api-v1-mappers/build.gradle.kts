plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))

    implementation(projects.beApiV1Jacson)

    implementation(projects.beCommon)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.beStubs)
}
