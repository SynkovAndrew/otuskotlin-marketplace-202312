package com.otus.otuskotlin.build

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin.BUILD_GROUP
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

internal abstract class PushDockerImageTask : DefaultTask() {

    @get:Input
    abstract val jarName: Property<String>

    @get:Input
    abstract val dockerRepositoryOwner: Property<String>

    init {
        group = BUILD_GROUP
        dependsOn.add(BUILD_DOCKER_IMAGE)
    }

    @TaskAction
    fun run() {
        with(project) {
            println("docker login ...")
            println(System.getenv("DOCKER_USERNAME"))
            println(System.getenv("DOCKER_PASSWORD"))
            println("...")
            runCommand(
                buildString {
                    append("echo ${System.getenv("DOCKER_PASSWORD")} | ")
                    append("docker login ")
                    append("-u ${System.getenv("DOCKER_USERNAME")} ")
                    append("--password-stdin")
                }
            )
            println("docker push ...")

            runCommand("docker image push ${dockerRepositoryOwner.get()}/${jarName.get()}:latest")
        }
    }
}
