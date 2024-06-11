plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-core-model"))
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }
}