package com.kronos.router

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import com.kronos.router.dsl.RouterDsl
import java.io.Serializable

@RouterDsl
class KRequest(
    val url: String,
    val bundle: Bundle? = null,
    val onSuccess: () -> Unit = {},
    val onFail: (exception: Exception) -> Unit = {
        throw  it
    }
) {

    var activityResultCode: Int = 0

    constructor(builder: Builder) : this(
        builder.url,
        builder.bundle,
        builder.onSuccess,
        builder.onFail
    ) {
        this.activityResultCode = builder.activityResultCode
    }


    fun start(context: Context) {
        Router.sharedRouter().open(this, context)
    }

    fun newBuilder(): Builder {
        val newBuilder = Builder(url)
        newBuilder.onSuccess = onSuccess
        newBuilder.onFail = onFail
        newBuilder.bundle = bundle
        newBuilder.activityResultCode = activityResultCode
        return newBuilder
    }

    @RouterDsl
    class Builder(val url: String) {

        var bundle: Bundle? = null
        var onSuccess: () -> Unit = {}
        var onFail: (exception: Exception) -> Unit = {
            throw  it
        }
        var activityResultCode: Int = 0

        fun putBundle(bundle: Bundle) {
            this.bundle = bundle
        }

        fun addValue(key: String, value: Int): Builder {
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle?.putInt(key, value)
            return this
        }

        fun addValue(key: String, value: String): Builder {
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle?.putString(key, value)
            return this
        }

        fun addValue(key: String, value: Float): Builder {
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle?.putFloat(key, value)
            return this
        }

        fun addValue(key: String, value: Parcelable): Builder {
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle?.putParcelable(key, value)
            return this
        }

        fun addValue(key: String, value: Serializable): Builder {
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle?.putSerializable(key, value)
            return this
        }


        fun build(): KRequest {
            return KRequest(this)
        }

    }

}

