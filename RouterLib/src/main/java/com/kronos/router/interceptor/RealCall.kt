package com.kronos.router.interceptor

import android.content.Context
import android.os.Bundle
import com.kronos.router.KRequest
import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.HostParams
import com.kronos.router.model.RouterParams
import com.kronos.router.utils.RouteConfiguration
import java.util.*

class RealCall(private val hostMap: Map<String, HostParams>, private val config: RouteConfiguration) {

    private val cachedRoutes: HashMap<String, RouterParams> = hashMapOf()

    @Throws(RouteNotFoundException::class)
    fun open(request: KRequest, context: Context) {
        getParamsWithInterceptorChain(request, context, request.bundle)
    }

    @Throws(RouteNotFoundException::class)
    private fun getParamsWithInterceptorChain(request: KRequest, context: Context, bundle: Bundle?) {
        val interceptors: MutableList<Interceptor> = ArrayList()
        interceptors.add(LazyInitInterceptor())
        interceptors.addAll(config.interceptors)
        interceptors.add(CacheInterceptor(cachedRoutes))
        interceptors.add(RouterInterceptor(cachedRoutes))
        val chain: Interceptor.Chain = RealInterceptorChain(interceptors, request, hostMap, 0, context)
        chain.proceed()
    }

}