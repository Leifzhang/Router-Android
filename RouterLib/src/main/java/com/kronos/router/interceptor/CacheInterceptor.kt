package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.RouterParams

class CacheInterceptor internal constructor(private val cachedRoutes: HashMap<String, RouterParams>) : BaseInterceptor() {


    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        val url = chain.url.url
        if (cachedRoutes[url] != null) {
            val params = cachedRoutes[url]
            params?.apply {
                cachedRoutes[chain.url.url] = params
                pathInterceptor(this, chain)
            }
            return
        } else {
            chain.proceed()
        }
    }

}