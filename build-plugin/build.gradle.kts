plugins {
    `kotlin-dsl`
}

group = "com.otus.otuskotlin.marketplace"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("com.otus.otuskotlin.marketplace.build-jvm") {
            id = "com.otus.otuskotlin.marketplace.build-jvm"
            implementationClass = "com.otus.otuskotlin.marketplace.JvmBuildPlugin"
        }
        create("com.otus.otuskotlin.marketplace.build-multiplatform") {
            id = "com.otus.otuskotlin.marketplace.build-multiplatform"
            implementationClass = "com.otus.otuskotlin.marketplace.MultiplatformBuildPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}