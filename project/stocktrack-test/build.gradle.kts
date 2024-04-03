plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":stocktrack-api-v1-model"))
    implementation(project(":stocktrack-internal-model"))
    implementation(libs.logback)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.core)
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

dockerCompose {
    dockerComposeWorkingDirectory = project.file("./docker")
    isRequiredBy(tasks.test)
}
