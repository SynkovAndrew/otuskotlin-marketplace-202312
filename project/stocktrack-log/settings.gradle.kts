rootProject.name = "stocktrack-log"

include(
    "stocktrack-log-model",
    "stocktrack-log-core",
    "stocktrack-log-logback",
    "stocktrack-log-socket",
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../../build-plugin")
    plugins {
        id("com.otus.otuskotlin.build.build-jvm") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}