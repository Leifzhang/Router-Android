package com.kronos.router.interceptor

import android.net.Uri
import android.text.TextUtils
import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.HostParams
import com.kronos.router.model.RouterOptions
import com.kronos.router.model.RouterParams
import com.kronos.router.utils.RouterUtils
import java.util.*

abstract class BaseInterceptor : Interceptor {

    @Throws(RouteNotFoundException::class)
    fun getParams(url: String, hosts: Map<String, HostParams>?): RouterParams? {
        val parsedUri = Uri.parse(url)
        val urlPath = if (TextUtils.isEmpty(parsedUri.path)) "" else parsedUri.path?.substring(1)
        val givenParts = urlPath?.split("/".toRegex())?.toTypedArray() ?: arrayOf()
        val params: MutableList<RouterParams> = ArrayList()
        val hostParams = hosts?.get(parsedUri.host)
                ?: throw RouteNotFoundException("No host found for url $url")
        for (entry in hostParams.routes.entries) {
            val routerParams = getRouterParams(entry, givenParts)
            if (routerParams != null) {
                params.add(routerParams)
            }
        }
        var routerParams = if (params.size == 1) params[0] else null
        if (params.size > 1) {
            for (param in params) {
                if (TextUtils.equals(param.realPath, urlPath)) {
                    routerParams = param
                    break
                }
            }
            if (routerParams == null) {
                params.sortWith(Comparator { o1, o2 ->
                    val o1Weight = o1.weight
                    val o2Weight = o2.weight
                    o1Weight.compareTo(o2Weight)
                })
                routerParams = params[0]
            }
        }
        if (routerParams == null) {
            throw RouteNotFoundException("No params found for url $url")
        }
        for (key in parsedUri.queryParameterNames) {
            routerParams.put(key, parsedUri.getQueryParameter(key))
        }
        routerParams.put("targetUrl", url)
        return routerParams
    }

    private fun getRouterParams(entry: Map.Entry<String, RouterOptions>, givenParts: Array<String>): RouterParams? {
        val routerParams = RouterParams()
        val routerUrl = cleanUrl(entry.key)
        val routerOptions = entry.value
        val routerParts = routerUrl.split("/".toRegex()).toTypedArray()
        if (routerParts.size != givenParts.size) {
            return null
        }
        var givenParams: Map<String, String>? = null
        try {
            givenParams = RouterUtils.urlToParamsMap(givenParts, routerParts)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (givenParams == null) {
            return null
        }
        routerParams.url = entry.key
        routerParams.weight = entry.value.weight
        givenParams.forEach { entry ->
            routerParams.put(entry.key, entry.value)
        }
        routerParams.interceptors.addAll(routerOptions.interceptors)
        routerParams.routerOptions = routerOptions
        return routerParams
    }

    private fun cleanUrl(url: String): String {
        return if (url.startsWith("/")) {
            url.substring(1)
        } else url
    }


    fun pathInterceptor(params: RouterParams, chain: Interceptor.Chain) {
        val interceptors: MutableList<Interceptor> = ArrayList()
        if (params.interceptors.isNotEmpty()) {
            interceptors.addAll(params.interceptors)
        }
        interceptors.add(LaunchInterceptor(params))
        val childChain: Interceptor.Chain = RealInterceptorChain(interceptors, chain.url, chain.hostParams,
                0, chain.context, chain.bundle)
        childChain.proceed()
    }
}