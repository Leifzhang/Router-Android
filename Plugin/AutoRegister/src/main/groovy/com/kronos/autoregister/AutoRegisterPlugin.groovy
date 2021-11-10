package com.kronos.autoregister

import com.android.build.gradle.AppPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoRegisterPlugin implements Plugin<Project> {

    private static final String EXT_NAME = "AutoRegister";

    @Override
    void apply(Project project) {
        project.getExtensions().create(EXT_NAME, AutoRegisterConfig.class);
        if (project.plugins.hasPlugin('com.android.application')) {
            project.android.registerTransform(new NewAutoRegisterTransform())
            project.afterEvaluate(new Action<Project>() {
                @Override
                void execute(Project newProject) {
                    AutoRegisterConfig config = (AutoRegisterConfig) newProject.getExtensions().findByName(EXT_NAME);
                    if (config == null) {
                        config = new AutoRegisterConfig()   
                    }
                    config.transform()
                }
            })
        }
        project.afterEvaluate {
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
        }
    }
}