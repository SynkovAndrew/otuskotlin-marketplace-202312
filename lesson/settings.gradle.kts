rootProject.name = "lesson"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(
    "module-1-lesson-1-initial-project",
    "module-1-lesson-2-data-types",
    "module-1-lesson-3-functions",
    "module-1-lesson-4-oop",
    "module-1-lesson-5-dsl",
    "module-2-lesson-6-coroutines",
    "module-2-lesson-7-sequence-flow",
    "module-2-lesson-8-multiplatform",
    "module-2-lesson-9-interoperability"
)

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}