plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":stocktrack-application-api"))
    implementation(project(":stocktrack-core"))
    implementation(project(":stocktrack-core-model"))
    implementation(project(":stocktrack-business"))
    implementation(project(":stocktrack-api-v1-mapper"))
    implementation(project(":stocktrack-api-v1-model-kotlin"))

    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-core")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-logback")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-model")

    implementation("org.apache.kafka:kafka-clients:3.7.0")
    implementation(libs.coroutines.core)
    implementation(libs.kotlin.json)
    implementation(libs.logback)

    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}