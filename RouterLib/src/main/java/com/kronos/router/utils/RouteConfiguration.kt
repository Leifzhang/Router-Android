package com.kronos.router.utils

import com.kronos.router.interceptor.Interceptor

class RouteConfiguration {

    internal val interceptors = mutableListOf<Interceptor>()

    var debugMode = true

    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }
}