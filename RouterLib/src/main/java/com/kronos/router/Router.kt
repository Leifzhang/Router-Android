package com.kronos.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kronos.router.exception.ContextNotProvided
import com.kronos.router.interceptor.Interceptor
import com.kronos.router.interceptor.RealCall
import com.kronos.router.loader.RouterRegistry
import com.kronos.router.model.HostParams
import com.kronos.router.model.RouterOptions
import com.kronos.router.model.RouterParams
import com.kronos.router.utils.RouteConfiguration
import java.util.*

class Router private constructor() {

    private var application: Application? = null
    private val hosts: MutableMap<String, HostParams> = HashMap()
    private val config = RouteConfiguration()
    private val realCall: RealCall = RealCall(hosts, config)

    fun attachApplication(context: Application?) {
        RouterRegistry.register()
        application = context
    }

    @JvmOverloads
    fun openExternal(url: String, context: Context? = application) {
        this.openExternal(url, null, context)
    }

    @JvmOverloads
    fun openExternal(url: String, extras: Bundle?, context: Context? = application) {
        if (context == null) {
            throw ContextNotProvided("You need to supply a context for Router "
                    + this.toString())
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        addFlagsToIntent(intent, context)
        if (extras != null) {
            intent.putExtras(extras)
        }
        context.startActivity(intent)
    }

    @JvmOverloads
    fun open(url: String, context: Context? = application) {
        this.open(url, null, context)
    }

    @JvmOverloads
    fun open(url: String, extras: Bundle?, context: Context? = application) {
        if (context == null) {
            throw ContextNotProvided("You need to supply a context for Router $this")
        }
        realCall.open(url, context, extras)
    }

    private fun addFlagsToIntent(intent: Intent, context: Context) {
        if (context === application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun intentFor(params: RouterParams): Intent {
        val options = params.routerOptions
        val intent = Intent()
        options?.defaultParams?.apply {
            intent.putExtras(this)
        }
        for ((key, value) in params.openParams) {
            intent.putExtra(key, value)
        }
        return intent
    }

    private fun intentFor(context: Context, params: RouterParams): Intent? {
        val options = params.routerOptions
        if (options?.callback != null) {
            return null
        }
        val intent = intentFor(params)
        options?.openClass?.let { intent.setClass(context, it) }
        addFlagsToIntent(intent, context)
        return intent
    }

    companion object {

        private val router by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Router()
        }

        @JvmStatic
        fun sharedRouter(): Router {  // 第一次检查
            return router
        }

        @JvmStatic
        fun map(url: String, callback: RouterCallback) {
            val options = RouterOptions()
            options.callback = callback
            map(url, null, options)
        }

        @JvmOverloads
        fun map(url: String, mClass: Class<out Activity>, targetFragment: Class<out Fragment>,
                bundle: Bundle? = null) {
            val options = RouterOptions(bundle)
            options.putParams("target", targetFragment.name)
            map(url, mClass, options)
        }

        @JvmStatic
        fun map(url: String, mClass: Class<out Activity>, interceptor: Interceptor) {
            val options = RouterOptions()
            options.addInterceptor(interceptor)
            map(url, mClass, options)
        }

        @JvmStatic
        fun map(url: String, mClass: Class<out Activity>, interceptor: Array<Interceptor>) {
            val options = RouterOptions()
            options.addInterceptors(interceptor)
            map(url, mClass, options)
        }

        @JvmStatic
        @JvmOverloads
        fun map(url: String, mClass: Class<out Activity>?, options: RouterOptions = RouterOptions()) {
            val uri = Uri.parse(url)
            uri?.path?.let {
                options.openClass = mClass
                val hostParams: HostParams?
                if (router.hosts.containsKey(uri.host)) {
                    router.hosts[uri.host]?.apply {
                        setRoute(it, options)
                    }
                } else {
                    uri.host?.let { host ->
                        hostParams = HostParams(host)
                        hostParams.setRoute(it, options)
                        router.hosts[hostParams.host] = hostParams
                    }
                }
            }
        }

        fun addGlobalInterceptor(interceptor: Interceptor) {
            router.config.addInterceptor(interceptor)
        }
    }

}