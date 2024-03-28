plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-api-v1-model"))
    implementation(project(":stocktrack-internal-model"))
}