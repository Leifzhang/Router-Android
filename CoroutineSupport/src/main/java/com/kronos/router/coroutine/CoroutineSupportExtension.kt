package com.kronos.router.coroutine

import android.content.Context
import com.kronos.router.KRequest
import com.kronos.router.dsl.fail
import com.kronos.router.dsl.newRequest
import com.kronos.router.dsl.request
import com.kronos.router.dsl.success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * @Author LiABao
 * @Since 2020/12/10
 */
suspend fun KRequest.dispatcher(context: Context): Boolean {
    return withContext(Dispatchers.Main) {
        val result = await(context)
        result
    }
}

suspend fun KRequest.await(context: Context): Boolean {
    return suspendCancellableCoroutine { continuation ->
        newRequest {
            success {
                continuation.resume(true)
            }
            fail {
                continuation.resume(false)
            }
        }.start(context)
    }
}