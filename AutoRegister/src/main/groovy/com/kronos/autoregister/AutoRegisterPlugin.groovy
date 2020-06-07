package com.kronos.autoregister

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoRegisterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        boolean isApp = project.getPlugins().hasPlugin(AppPlugin.class)
        if (isApp) {
            project.android.registerTransform(new NewAutoRegisterTransform())
        }
    }
}