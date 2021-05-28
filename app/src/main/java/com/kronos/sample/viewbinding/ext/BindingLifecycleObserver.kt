package com.kronos.sample.viewbinding.ext

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * <pre>
 *     author: dhl
 *     date  : 2021/1/25
 *     desc  :
 * </pre>
 */
interface BindingLifecycleObserver : LifecycleObserver {

    fun onCreate(owner: LifecycleOwner) {

    }

    fun onDestroy(owner: LifecycleOwner) {
    }

}