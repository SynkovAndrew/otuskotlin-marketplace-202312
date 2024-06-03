plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-api-v1-model-kotlin")
    implementation(project(":stocktrack-core-model"))
    implementation(libs.kotlinx.datetime)

    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}