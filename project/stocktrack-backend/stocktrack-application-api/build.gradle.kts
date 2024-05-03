plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-core"))
    implementation(project(":stocktrack-core-model"))
    implementation(project(":stocktrack-business"))
    implementation(project(":stocktrack-log-v1-mapper"))
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-core")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-model")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}