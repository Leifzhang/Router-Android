package com.kronos.router.interceptor

import android.content.Context
import android.os.Bundle
import com.kronos.router.exception.RouteNotFoundException
import com.kronos.router.model.HostParams

interface Interceptor {
    @Throws(RouteNotFoundException::class)
    fun intercept(chain: Chain)

    interface Chain {

        val url: String

        val context: Context

        val bundle: Bundle?

        val hostParams: Map<String, HostParams>?

        @Throws(RouteNotFoundException::class)
        fun proceed()
    }
}