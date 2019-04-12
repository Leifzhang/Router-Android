package com.kronos.router.model

/**
 * Created by zhangyang on 16/7/16.
 */
class RouterParams {
    var url: String? = null
    private var weight: Int = 0
    var routerOptions: RouterOptions? = null
    var openParams: Map<String, String>? = null

    val realPath: String
        get() {
            try {

                return url!!.subSequence(1, url!!.length).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

    fun getWeight(): Int? {
        return weight
    }

    fun setWeight(weight: Int) {
        this.weight = weight
    }
}
