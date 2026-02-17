plugins {
    application
    id("build-jvm")
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("com.github.watching1981.app.kafka.MainKt")
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)
    implementation("com.github.watching1981.car:car-lib-logging-logback")

    implementation(project(":be-app-common"))
    implementation(projects.beRepoStubs)
    implementation(projects.beRepoInmemory)
    implementation(projects.beRepoPgjvm)
    testImplementation(projects.beRepoCommon)
    testImplementation(projects.beStubs)


    // transport models
    implementation(project(":be-common"))
    implementation(project(":be-api-v1-jacson"))
    implementation(project(":be-api-v1-mappers"))
    implementation(project(":be-api-v2-kmp"))
    // logic
    implementation(project(":be-biz"))

    testImplementation(kotlin("test-junit"))
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}

