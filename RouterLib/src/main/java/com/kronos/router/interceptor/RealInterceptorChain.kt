package com.kronos.router.interceptor

import android.content.Context
import android.os.Bundle
import com.kronos.router.KRequest
import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.HostParams

class RealInterceptorChain internal constructor(private val interceptors: List<Interceptor>, override val url: KRequest,
                                                override val hostParams: Map<String, HostParams>?,
                                                private val index: Int, override
                                                val context: Context) : Interceptor.Chain {

    @Throws(RouteNotFoundException::class)
    override fun proceed() {
        proceed(url)
    }

    @Throws(RouteNotFoundException::class)
    fun proceed(request: KRequest) {
        if (index >= interceptors.size) {
            return
        }
        val next = RealInterceptorChain(interceptors, request, hostParams,
                index + 1, context)
        val interceptor = interceptors[index]
        interceptor.intercept(next)
    }

}