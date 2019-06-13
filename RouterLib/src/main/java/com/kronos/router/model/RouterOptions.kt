package com.kronos.router.model

import android.app.Activity
import android.os.Bundle

import com.kronos.router.RouterCallback

/**
 * Created by zhangyang on 16/7/16.
 */
class RouterOptions {
    var openClass: Class<out Activity>? = null
    var callback: RouterCallback? = null
    private var _defaultParams: Bundle? = null
    var weight = 0

    var defaultParams: Bundle?
        get() = this._defaultParams
        set(defaultParams) {
            if (_defaultParams == null) {
                _defaultParams = Bundle()
            }
            if (defaultParams != null) {
                _defaultParams!!.putAll(defaultParams)
            }
        }

    constructor() {
        if (_defaultParams == null) {
            _defaultParams = Bundle()
        }
    }


    constructor(defaultParams: Bundle) {
        this.defaultParams = defaultParams
    }


    fun putParams(key: String, value: String) {
        _defaultParams!!.putString(key, value)
    }
}
