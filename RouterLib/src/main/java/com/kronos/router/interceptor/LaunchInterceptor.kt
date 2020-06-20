package com.kronos.router.interceptor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.kronos.router.RouterContext
import com.kronos.router.model.RouterParams
import com.kronos.router.utils.IntentUtils

class LaunchInterceptor(private val params: RouterParams) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) {
        process(chain.bundle, chain.context)
    }

    private fun process(extras: Bundle?, context: Context) {
        val options = params.routerOptions
        if (options?.callback != null) {
            RouterContext(params.openParams, extras, context).apply {
                options.callback?.run(this)
            }
            return
        }
        val intent: Intent = IntentUtils.intentFor(context, params)
                ?: return
        extras?.apply { intent.putExtras(this) }
        context.startActivity(intent)
    }

}