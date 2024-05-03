plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.apache.kafka:kafka-clients:3.7.0")
    implementation(libs.coroutines.core)
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