rootProject.name = "stocktrack-lib"

include(
    "stocktrack-lib-chain-of-responsibility",
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