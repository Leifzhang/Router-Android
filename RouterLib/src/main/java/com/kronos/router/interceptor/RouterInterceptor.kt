package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.RouterParams

class RouterInterceptor(private val cachedRoutes: HashMap<String, RouterParams>) : BaseInterceptor() {

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        val params = getParams(chain.url, chain.hostParams)
        params?.apply {
            cachedRoutes[chain.url] = params
            pathInterceptor(this, chain)
        }
    }
}