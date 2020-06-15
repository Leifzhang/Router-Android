package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.RouterParams

class CacheInterceptor internal constructor() : Interceptor {
    private val cachedRoutes: HashMap<String, RouterParams> = hashMapOf()

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        val url = chain.url()
        if (cachedRoutes[url] != null) {
            val params = cachedRoutes[url]
            return
        }
        chain.proceed()
    }

}