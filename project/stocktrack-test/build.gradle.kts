plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":stocktrack-api-v1-model"))
    implementation(project(":stocktrack-internal-model"))
    implementation("ch.qos.logback:logback-classic:1.5.3")

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation("io.ktor:ktor-serialization-jackson:2.3.9")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.9")

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.core)
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

dockerCompose {
    dockerComposeWorkingDirectory = project.file("./docker")
    isRequiredBy(tasks.test)
}
