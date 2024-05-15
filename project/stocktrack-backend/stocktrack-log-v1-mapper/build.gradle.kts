plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-model")
    implementation(project(":stocktrack-api-v1-model-kotlin"))
    implementation(project(":stocktrack-core-model"))

    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}