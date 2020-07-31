package com.kronos.router.utils

/**
 * Created by Leif Zhang on 2016/12/6.
 * Email leifzhanggithub@gmail.com
 */
object RouterBind {

    @JvmStatic
    fun bind(className: String?) {
        try {
            val routerInit = className?.let { Class.forName(it) }
            val method = routerInit?.getMethod("init")
            method?.invoke(null)
        } catch (e: Exception) {

        }
    }
}