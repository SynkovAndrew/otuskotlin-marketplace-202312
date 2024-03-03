rootProject.name = "project"

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
        id("com.otus.otuskotlin.marketplace.build-jvm") apply false
        id("com.otus.otuskotlin.marketplace.build-multiplatform") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    "multiplatform-sample",
)