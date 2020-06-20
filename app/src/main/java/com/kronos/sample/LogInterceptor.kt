package com.kronos.sample

import android.util.Log
import com.kronos.router.interceptor.Interceptor

class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain) {
        Log.i("LogInterceptor", "LogInterceptor")
        chain.proceed()
    }
}