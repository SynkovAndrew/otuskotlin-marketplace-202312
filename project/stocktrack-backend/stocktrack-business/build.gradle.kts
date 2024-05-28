plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-lib-chain-of-responsibility")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-core")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-model")

    implementation(project(":stocktrack-core-model"))
    implementation(project(":stocktrack-core"))

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}