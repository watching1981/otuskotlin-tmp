plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
//            implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginJvm"
            implementationClass = "com.github.watching1981.plugin.BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            //implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginMultiplatform"
            implementationClass = "com.github.watching1981.plugin.BuildPluginMultiplatform"
        }
        register("build-docker") {
            id = "build-docker"
            implementationClass = "com.github.watching1981.plugin.DockerPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.binaryCompatibilityValidator)

}
