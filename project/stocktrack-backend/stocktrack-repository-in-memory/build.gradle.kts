plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-repository-core"))
    implementation(project(":stocktrack-core-model"))

    implementation(libs.cache4k)
    implementation(libs.coroutines.core)

    testImplementation(project(":stocktrack-repository-core-test"))
    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
    testImplementation(libs.coroutines.test)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}