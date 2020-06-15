package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException

class RouterInterceptor(private val interceptors: MutableList<Interceptor>) : BaseLaunchInterceptor() {

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        val params = getParams(chain.url(), chain.hostParams)
        params?.apply {
            interceptors.addAll(params.interceptors)
            chain.proceed()
            launch(params, chain.bundle, chain.context)
        }
    }
}