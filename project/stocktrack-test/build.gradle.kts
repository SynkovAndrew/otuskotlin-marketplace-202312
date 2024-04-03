plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.kotlin.serialization)
}

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "com.otus.otuskotlin.stocktrack"
    version = "0.0.1"
}

dependencies {
  //  implementation("com.otus.otuskotlin.stocktrack:stocktrack-api-v1-model")
  //  implementation("com.otus.otuskotlin.stocktrack:stocktrack-internal-model")
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
