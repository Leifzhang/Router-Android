package com.kronos.sample.viewbinding.viewbind

import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.kronos.sample.viewbinding.base.ActivityDelegate
import com.kronos.sample.viewbinding.ext.inflateMethod
import kotlin.reflect.KProperty

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/12/10
 *     desc  :
 * </pre>
 */

class ActivityViewBinding<T : ViewBinding>(
    classes: Class<T>,
    activity: Activity
) : ActivityDelegate<T>(activity) {

    private var layoutInflater = classes.inflateMethod()

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return viewBinding?.run {
            this
        } ?: let {
            // 获取 ViewBinding 实例
            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)
            return bind.apply { viewBinding = this }
        }
    }

}