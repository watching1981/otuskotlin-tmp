plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group =  "com.github.watching1981"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    val buildMigrations: Task by creating {
        dependsOn(gradle.includedBuild("car-other").task(":buildInfra"))
    }

    val buildImages: Task by creating {
        dependsOn(gradle.includedBuild("FilippovMAproject").task(":buildImages"))
        mustRunAfter(buildMigrations)
    }
    val e2eTests: Task by creating {
        dependsOn(gradle.includedBuild("car-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    register("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("FilippovMAproject").task(":check"))
        dependsOn(e2eTests)
    }
}
