plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.docker.compose)
}

dependencies {
    implementation(project(":stocktrack-repository-core"))
    implementation(project(":stocktrack-core-model"))

    implementation(libs.postgresql)
    implementation(libs.bundles.kotlin.exposed)
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
    testImplementation(libs.coroutines.test)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed")
    }

    dependsOn(tasks.composeUp)
    finalizedBy(tasks.composeDown)
}

dockerCompose {
    dockerComposeWorkingDirectory = project.file("./docker")
    executable = "/Applications/Docker.app/Contents/Resources/bin/docker-compose"
    dockerExecutable = "/Applications/Docker.app/Contents/Resources/bin/docker"
}