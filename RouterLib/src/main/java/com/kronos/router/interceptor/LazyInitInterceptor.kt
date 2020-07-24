package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.loader.RouterRegistry

class LazyInitInterceptor : Interceptor {
    private var isRegister = false

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        if (!isRegister) {
            RouterRegistry.register()
            isRegister = true
        }
        chain.proceed()
    }
}