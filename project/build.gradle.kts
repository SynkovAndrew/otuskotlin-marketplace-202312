plugins {
    id("com.otus.otuskotlin.marketplace.build-jvm") apply false
}

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "com.otus.otuskotlin.marketplace"
    version = "0.0.1"
}