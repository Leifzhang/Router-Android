package com.kronos.router.interceptor

import com.kronos.router.exception.RouteNotFoundException

class RouterInterceptor : BaseInterceptor() {

    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        val params = getParams(chain.url, chain.hostParams)
        params?.apply {
            pathInterceptor(this, chain)

        }
    }
}