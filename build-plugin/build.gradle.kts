plugins {
    `kotlin-dsl`
}

group = "com.otus.otuskotlin.build"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("com.otus.otuskotlin.build.build-jvm") {
            id = "com.otus.otuskotlin.build.build-jvm"
            implementationClass = "com.otus.otuskotlin.build.JvmBuildPlugin"
        }
        create("com.otus.otuskotlin.build.build-multiplatform") {
            id = "com.otus.otuskotlin.build.build-multiplatform"
            implementationClass = "com.otus.otuskotlin.build.MultiplatformBuildPlugin"
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