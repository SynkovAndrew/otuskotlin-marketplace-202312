plugins {
    id("com.otus.otuskotlin.build.build-jvm") apply false
}

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "com.otus.otuskotlin.stocktrack"
    version = "0.0.1"
}