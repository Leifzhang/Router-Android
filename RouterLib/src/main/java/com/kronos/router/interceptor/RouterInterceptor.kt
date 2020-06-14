package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException

class RouterInterceptor : BaseLaunchInterceptor() {

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        val params = getParams(chain.url(), chain.hostParams)
        params?.apply {
            launch(params, chain.bundle, chain.context)
        }
    }
}