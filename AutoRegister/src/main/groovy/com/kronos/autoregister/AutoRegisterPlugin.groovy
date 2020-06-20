package com.kronos.autoregister

import com.android.build.gradle.AppPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoRegisterPlugin implements Plugin<Project> {

    private static final String EXT_NAME = "AutoRegister";

    @Override
    void apply(Project project) {
        boolean isApp = project.getPlugins().hasPlugin(AppPlugin.class)
        project.getExtensions().create(EXT_NAME, AutoRegisterConfig.class);
        if (isApp) {
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
            });
        }
    }
}