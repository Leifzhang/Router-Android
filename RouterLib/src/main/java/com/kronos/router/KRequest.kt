package com.kronos.router

import android.content.Context
import android.os.Bundle
import com.kronos.router.exception.RouteNotFoundException

class KRequest(val url: String,
               val bundle: Bundle? = null,
               var onSuccess: () -> Unit = {},
               var onFail: (exception: RouteNotFoundException) -> Unit = {
                   throw  it
               }
) {

    fun start(context: Context) {
        Router.sharedRouter().open(this, context)
    }
}