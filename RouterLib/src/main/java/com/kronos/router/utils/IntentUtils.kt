package com.kronos.router.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.kronos.router.RouterContext
import com.kronos.router.model.RouterParams

object IntentUtils {

    internal fun intentFor(params: RouterParams): Intent {
        val options = params.routerOptions
        val intent = Intent()
        options?.defaultParams?.apply {
            intent.putExtras(this)
        }
        params.openParams?.forEach() {
            intent.putExtra(it.key, it.value)
        }
        return intent
    }


    internal fun intentFor(context: Context, params: RouterParams): Intent? {
        val options = params.routerOptions
        if (options!!.callback != null) {
            return null
        }
        val intent = intentFor(params)
        options.openClass?.apply {
            intent.setClass(context, this)
        }
        this.addFlagsToIntent(intent, context)
        return intent
    }

    internal fun addFlagsToIntent(intent: Intent, context: Context) {
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}