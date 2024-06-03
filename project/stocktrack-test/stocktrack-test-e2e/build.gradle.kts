plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-api-v1-model-kotlin")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-core-model")
    implementation(libs.logback)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.9")

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.core)
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }

    dependsOn(tasks.composeUp)
    finalizedBy(tasks.composeDown)
}

dockerCompose {
    dockerComposeWorkingDirectory = project.file("./docker")
/*    executable = "/Applications/Docker.app/Contents/Resources/bin/docker-compose"
    dockerExecutable = "/Applications/Docker.app/Contents/Resources/bin/docker"*/
}
