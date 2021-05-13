package com.kronos.sample

import com.kronos.router.interceptor.Interceptor
import kotlinx.coroutines.*

/**
 *
 *  @Author LiABao
 *  @Since 2021/3/31
 *
 */
class DelayInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain) {
        GlobalScope.launch {
            delay(5000)
            withContext(Dispatchers.Main) {
                chain.proceed()
            }
        }
    }
}