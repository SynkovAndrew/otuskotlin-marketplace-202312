plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(project(":stocktrack-repository-core"))
    implementation(project(":stocktrack-core-model"))

    implementation(libs.slf4j.api)
    implementation(libs.coroutines.jdk9)
    implementation(libs.bundles.cassandra)
    kapt(libs.cassandra.kapt)
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