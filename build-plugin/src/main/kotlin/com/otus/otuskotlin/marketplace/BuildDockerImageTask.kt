package com.otus.otuskotlin.marketplace

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin.BUILD_GROUP
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

internal abstract class BuildDockerImageTask : DefaultTask() {
    private val buildDirectory = "${project.projectDir}/build/libs"

    @get:Input
    abstract val jarName: Property<String>

    init {
        group = BUILD_GROUP
        dependsOn(BUILD_JVM)
    }

    @TaskAction
    fun run() {
        createDockerfile()

        with(project) {
            runCommand("docker build -t ${jarName.get()} $buildDirectory")
        }
    }

    private fun createDockerfile() {
        File("${buildDirectory}/Dockerfile")
            .printWriter()
            .use {
                it.println(
                    """
                        FROM openjdk:17-jdk-slim
                        MAINTAINER Gradle Task \"$name\"
                        EXPOSE 8080
                        COPY ./${jarName.get()}.$JAR_FILE_EXTENSION /home/${jarName.get()}.$JAR_FILE_EXTENSION
                        CMD ["java", "-jar", "/home/${jarName.get()}.$JAR_FILE_EXTENSION", "${jvmArgs()}"]
                    """.trimIndent()
                )
            }
    }

    private fun jvmArgs(): String {
        return "-Dlogback.configurationFile=/home/logback.xml"
    }
}
