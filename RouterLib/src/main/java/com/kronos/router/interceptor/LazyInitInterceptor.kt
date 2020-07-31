package com.kronos.router.interceptor

import android.content.Context
import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.loader.RouterRegistry
import com.kronos.router.utils.ClassUtils
import com.kronos.router.utils.RouterBind

class LazyInitInterceptor : Interceptor {
    private var isRegister = false

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        if (!isRegister) {
            try {
                RouterRegistry.register()
            } catch (e: Exception) {
                initByClassLoader(chain.context)
            }
            isRegister = true
        }
        chain.proceed()
    }

    private fun initByClassLoader(context: Context) {
        try {
            val classFileNames: List<String> = ClassUtils.getFileNameByPackageName(context, RouterPathPackage)
            for (className in classFileNames) {
                RouterBind.bind(className)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val RouterPathPackage = "com.kronos.router.init"
    }
}