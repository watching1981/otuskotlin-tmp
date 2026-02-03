plugins {
    id("build-docker") apply false
}

group = "com.github.watching1981.tests"
version = "0.1.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("buildInfra") {
        group = "build"
//        dependsOn(project(":other-dcompose").getTasksByName("publish",false))
        dependsOn(project(":other-migration-cs").getTasksByName("buildImage",false))
        dependsOn(project(":other-migration-pg").getTasksByName("buildImage",false))
    }
}
