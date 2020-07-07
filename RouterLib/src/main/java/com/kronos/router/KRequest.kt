package com.kronos.router

import android.content.Context
import android.os.Bundle
import com.kronos.router.exception.RouteNotFoundException
import java.lang.Exception

class KRequest(val url: String,
               val bundle: Bundle? = null,
               var onSuccess: () -> Unit = {},
               var onFail: (exception: Exception) -> Unit = {
                   throw  it
               }
) {
    var activityResultCode: Int = 0

    fun start(context: Context) {
        Router.sharedRouter().open(this, context)
    }
}