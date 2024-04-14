plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":stocktrack-log-core"))

    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(libs.ktor.network)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.atomicfu)

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
}
