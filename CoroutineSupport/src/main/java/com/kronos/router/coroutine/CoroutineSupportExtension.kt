package com.kronos.router.coroutine

import android.content.Context
import com.kronos.router.KRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * @Author LiABao
 * @Since 2020/12/10
 */
suspend fun KRequest.await(context: Context): Boolean {
    return suspendCancellableCoroutine { continuation ->
        onSuccess = {
            continuation.resume(true)
        }
        onFail = {
            continuation.resume(false)
        }
        start(context)
    }
}