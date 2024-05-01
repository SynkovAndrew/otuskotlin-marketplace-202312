plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-core-model"))
    implementation(project(":stocktrack-core"))
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}