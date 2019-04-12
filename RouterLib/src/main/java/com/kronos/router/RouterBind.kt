package com.kronos.router

import java.lang.reflect.Method

/**
 * Created by Leif Zhang on 2016/12/6.
 * Email leifzhanggithub@gmail.com
 */

object RouterBind {
    fun bind(className: String) {
        try {
            val routerInit = Class.forName(className)
            val method = routerInit.getMethod("init")
            method.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
