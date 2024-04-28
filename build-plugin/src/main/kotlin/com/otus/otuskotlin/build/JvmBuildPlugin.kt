package com.otus.otuskotlin.build

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.BasePlugin.BUILD_GROUP
import org.gradle.api.provider.Property
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

interface BuildJvmExtension {
    val mainClass: Property<String>
    val jarName: Property<String>
    val dockerImageGroup: Property<String>
}

internal class JvmBuildPlugin : Plugin<Project> {
    private lateinit var buildJvmExtension: BuildJvmExtension

    override fun apply(target: Project) {
        with(target) {
            buildJvmExtension = extensions.create<BuildJvmExtension>(BUILD_JVM)

            pluginManager.apply(JVM_PLUGIN)
            group = rootProject.group
            version = rootProject.version

            tasks.register(BUILD_JVM, Jar::class, buildJvmConfiguration())

            tasks.register<BuildDockerImageTask>(BUILD_DOCKER_IMAGE) {
                jarName.set(buildJvmExtension.jarName)
            }
     /*       tasks.register<PushDockerImageTask>(PUSH_DOCKER_IMAGE) {
                jarName.set(buildJvmExtension.jarName)
                dockerImageGroup.set(buildJvmExtension.dockerImageGroup)
            }*/
        }
    }

    private fun Project.buildJvmConfiguration(): Jar.() -> Unit {
        return {
            dependsOn(configurationsRuntimeClasspath())

            group = BUILD_GROUP
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            manifest.attributes[MAIN_CLASS_MANIFEST_ATTRIBUTE] = buildJvmExtension.mainClass.orNull
                ?: throw GradleException("Main class is not provided in buildJvm")
            archiveBaseName.set(buildJvmExtension.jarName)
            archiveExtension.set(JAR_FILE_EXTENSION)
            archiveAppendix.set("")
            archiveVersion.set("")
            archiveClassifier.set("")

            from(
                configurationsRuntimeClasspath()
                    .map { item -> item.takeIf { it.isDirectory } ?: zipTree(item) }
            )
            from(mainSourceSet().output)
        }
    }
}
