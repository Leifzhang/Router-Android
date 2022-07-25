package com.kronos.autoregister

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoRegisterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(EXT_NAME, AutoRegisterConfig::class.java)
        if (project.plugins.hasPlugin("com.android.application")) {
            val appExtension = project.extensions.getByType(
                AppExtension::class.java
            )
            appExtension.registerTransform(NewAutoRegisterTransform())
            project.afterEvaluate { newProject ->
                var config = newProject.extensions.findByName(EXT_NAME) as AutoRegisterConfig?
                if (config == null) {
                    config = AutoRegisterConfig()
                }
                config.transform()
            }
        }
        /*  project.afterEvaluate {
            if (project.plugins.hasPlugin('com.android.library')) {
                def android = project.extensions.getByName('android')
                android.defaultConfig.javaCompileOptions.annotationProcessorOptions {
                    arguments = [ROUTER_MODULE_NAME: project.getName()]
                }
                if (project.plugins.hasPlugin('kotlin-kapt')) {
                    def kapt = project.extensions.getByName('kapt')
                    kapt.arguments {
                        arg("ROUTER_MODULE_NAME", project.getName())
                    }
                }
            }
        }*/
    }

    companion object {
        private const val EXT_NAME = "AutoRegister"
    }
}