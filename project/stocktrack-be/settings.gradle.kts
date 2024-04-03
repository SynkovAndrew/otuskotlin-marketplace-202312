rootProject.name = "stocktrack-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("com.otus.otuskotlin.build.build-jvm") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    "stocktrack-bootstrap",
    "stocktrack-api-v1-model",
    "stocktrack-api-v1-mapper",
    "stocktrack-internal-model"
)