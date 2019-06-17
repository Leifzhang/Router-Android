package com.kronos.autoregister

import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoRegisterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.android.registerTransform(new AutoRegisterTransform(project))
    }
}