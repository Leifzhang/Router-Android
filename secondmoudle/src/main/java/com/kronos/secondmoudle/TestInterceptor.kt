package com.kronos.secondmoudle

import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.interceptor.Interceptor

class TestInterceptor : Interceptor {
    @Throws(RouteNotFoundException::class)
    override fun intercept(chain: Interceptor.Chain) {
        chain.proceed()
    }
}