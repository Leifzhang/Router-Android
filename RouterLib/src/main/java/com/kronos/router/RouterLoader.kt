package com.kronos.router

import android.app.Application

import com.kronos.router.utils.ClassUtils

/**
 * Created by  Leif Zhang on 2017/8/28.
 * Email leifzhanggithub@gmail.com
 */

internal class RouterLoader {

    var isLoadingFinish = false
        private set

    fun attach(context: Application) {
        try {
            val classFileNames = ClassUtils.getFileNameByPackageName(context, RouterPathPackage)
            for (className in classFileNames) {
                RouterBind.bind(className)
            }
            isLoadingFinish = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        private const val RouterPathPackage = "com.kronos.router.init"
    }
}
