package com.kronos.router.dsl

import android.os.Bundle
import com.kronos.router.KRequest

/**
 * @Author LiABao
 * @Since 2021/5/13
 */

fun request(url: String, build: KRequest.Builder.() -> Unit): KRequest {
    val requestBuilder = KRequest.Builder(url)
    build.invoke(requestBuilder)
    return requestBuilder.build()
}

fun KRequest.newRequest(build: KRequest.Builder.() -> Unit): KRequest {
    val builder = newBuilder()
    build.invoke(builder)
    return builder.build()
}


fun KRequest.Builder.bundle(invoke: Bundle.() -> Unit) {
    if (bundle == null) {
        bundle = Bundle()
    }
    bundle?.apply(invoke)
}

@RouterDsl
fun KRequest.Builder.success(invoke: () -> Unit) {
    onSuccess = invoke
}

@RouterDsl
fun KRequest.Builder.fail(invoke: (exception: Exception) -> Unit) {
    onFail = invoke
}