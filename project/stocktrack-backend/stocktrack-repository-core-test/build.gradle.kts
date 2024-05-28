plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-core-model"))
    implementation(project(":stocktrack-repository-core"))
    implementation(kotlin("test-junit"))
    implementation(libs.assertj)
    implementation(libs.coroutines.test)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}