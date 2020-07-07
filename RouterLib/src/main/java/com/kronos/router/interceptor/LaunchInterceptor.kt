package com.kronos.router.interceptor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.KRequest
import com.kronos.router.RouterContext
import com.kronos.router.model.RouterParams
import com.kronos.router.utils.FragmentForResult
import com.kronos.router.utils.IntentUtils
import java.lang.NullPointerException

class LaunchInterceptor(private val params: RouterParams) : Interceptor {

    override fun intercept(chain: Interceptor.Chain) {
        val request = chain.url
        process(request, request.bundle, chain.context)
    }

    private fun process(request: KRequest, extras: Bundle?, context: Context) {
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
        if (request.activityResultCode == 0 || context !is AppCompatActivity) {
            context.startActivity(intent)
            request.onSuccess.invoke()
        } else {
            val activity = context as AppCompatActivity
            options?.openClass?.apply {
                FragmentForResult.startActivityForResult(request.activityResultCode, this, activity, extras,
                        onSuccess = request.onSuccess, onFail = {
                    request.onFail.invoke(NullPointerException(""))
                }
                )
            }


        }
    }

}