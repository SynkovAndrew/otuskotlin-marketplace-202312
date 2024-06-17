plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":stocktrack-log-v1-mapper"))
    implementation(project(":stocktrack-business"))
    implementation(project(":stocktrack-core"))
    implementation(project(":stocktrack-core-model"))
    implementation(project(":stocktrack-api-v1-model-kotlin"))
    implementation(project(":stocktrack-api-v1-mapper"))
    implementation(project(":stocktrack-application-api"))
    implementation(project(":stocktrack-repository-core"))
    implementation(project(":stocktrack-repository-in-memory"))
    implementation(project(":stocktrack-repository-stub"))
    implementation(project(":stocktrack-repository-postgresql"))
    implementation(project(":stocktrack-repository-cassandra"))

    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-core")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-logback")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-model")

    implementation(libs.kotlin.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.datetime)

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-client-apache")
    implementation("io.ktor:ktor-client-okhttp-jvm")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-cors")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test"))
}

buildJvm {
    mainClass = "com.otus.otuskotlin.stocktrack.StockTrackApplicationKt"
    jarName = "stocktrack-backend"
    dockerRepositoryOwner = "andrewsynkov"
}

application {
    mainClass = "com.otus.otuskotlin.stocktrack.StockTrackApplicationKt"
}

tasks.test {
    useJUnitPlatform()
}
