package com.kronos.router

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

class KRequest(val url: String,
               var bundle: Bundle? = null,
               var onSuccess: () -> Unit = {},
               var onFail: (exception: Exception) -> Unit = {
                   throw  it
               }
) {

    var activityResultCode: Int = 0

    fun addValue(key: String, value: Int): KRequest {
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle?.putInt(key, value)
        return this
    }

    fun addValue(key: String, value: String): KRequest {
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle?.putString(key, value)
        return this
    }

    fun addValue(key: String, value: Float): KRequest {
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle?.putFloat(key, value)
        return this
    }

    fun addValue(key: String, value: Parcelable): KRequest {
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle?.putParcelable(key, value)
        return this
    }

    fun addValue(key: String, value: Serializable): KRequest {
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle?.putSerializable(key, value)
        return this
    }

    fun start(context: Context) {
        Router.sharedRouter().open(this, context)
    }
}