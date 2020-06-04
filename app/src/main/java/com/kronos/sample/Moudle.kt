package com.kronos.sample

import android.util.Log
import com.kronos.router.BindModule
import com.kronos.router.BindRouter
import com.kronos.router.init.RouterInit_app

/**
 * Created by Leif Zhang on 2016/12/5.
 * Email leifzhanggithub@gmail.com
 */
@BindModule("app")
object Moudle {
    fun init() {
        try {
            RouterInit_app.init()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            RouterInit_app.init()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}