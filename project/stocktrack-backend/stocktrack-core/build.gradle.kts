plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-core-model"))
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-core")

    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}