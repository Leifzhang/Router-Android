package com.kronos.router.interceptor

import android.content.Context
import android.os.Bundle
import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.HostParams
import com.kronos.router.model.RouterParams
import com.kronos.router.utils.RouteConfiguration
import java.util.*

class RealCall(private val hostMap: Map<String, HostParams>, private val config: RouteConfiguration) {

    private val cachedRoutes: HashMap<String, RouterParams> = hashMapOf()

    @Throws(RouteNotFoundException::class)
    fun open(url: String, context: Context, bundle: Bundle?) {
        getParamsWithInterceptorChain(url, context, bundle)
    }

    @Throws(RouteNotFoundException::class)
    private fun getParamsWithInterceptorChain(url: String, context: Context, bundle: Bundle?) {
        val interceptors: MutableList<Interceptor> = ArrayList()
        interceptors.addAll(config.interceptors)
        interceptors.add(TestInterceptor())
        interceptors.add(CacheInterceptor(cachedRoutes))
        interceptors.add(RouterInterceptor(cachedRoutes))
        val chain: Interceptor.Chain = RealInterceptorChain(interceptors, url, hostMap, 0, context, bundle)
        chain.proceed()
    }

}