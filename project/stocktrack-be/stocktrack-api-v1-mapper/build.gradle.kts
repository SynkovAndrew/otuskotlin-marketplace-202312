plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-api-v1-model"))
    implementation(project(":stocktrack-internal-model"))

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}