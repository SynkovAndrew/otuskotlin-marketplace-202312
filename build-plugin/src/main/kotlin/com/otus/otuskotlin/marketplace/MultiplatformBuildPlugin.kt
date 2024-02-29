package com.otus.otuskotlin.marketplace

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

internal class MultiplatformBuildPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libs = target.the<LibrariesForLibs>()

        with(target) {
            pluginManager.apply(MULTIPLATFORM_PLUGIN)
            group = rootProject.group
            version = rootProject.version

            extensions.configure<KotlinMultiplatformExtension> {
                jvm { }
                macosX64("macos") {
                    binaries {
                        framework {
                            baseName = "Demo"
                        }
                    }
                }

                sourceSets.jvmTest {
                    dependencies {
                        implementation(libs.coroutines.test)
                        implementation(kotlin("test"))
                    }
                }
            }
        }
    }
}
