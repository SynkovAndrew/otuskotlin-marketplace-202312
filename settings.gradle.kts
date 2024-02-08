pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "otuskotlin-marketplace-202312"

include(
    "module-1-lesson-1-initial-project",
    "module-1-lesson-2-data-types",
    "module-1-lesson-3-functions",
    "module-1-lesson-4-oop",
    "module-1-lesson-5-dsl",
    "module-2-lesson-6-coroutines",
)