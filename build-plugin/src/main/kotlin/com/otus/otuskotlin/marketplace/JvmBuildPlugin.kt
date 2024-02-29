package com.otus.otuskotlin.marketplace

import org.gradle.api.Plugin
import org.gradle.api.Project

internal class JvmBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(JVM_PLUGIN)
            group = rootProject.group
            version = rootProject.version
        }
    }
}
