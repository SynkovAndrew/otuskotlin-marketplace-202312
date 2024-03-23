plugins {
    id("com.otus.otuskotlin.build.build-jvm") apply false
    id("com.otus.otuskotlin.build.build-multiplatform") apply false
}

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "com.otus.otuskotlin.sample"
    version = "0.0.1"
}