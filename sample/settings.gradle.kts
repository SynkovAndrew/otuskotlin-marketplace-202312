rootProject.name = "sample"

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
        id("com.otus.otuskotlin.build.build-multiplatform") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    "sample-jvm",
    "sample-multiplatform"
)