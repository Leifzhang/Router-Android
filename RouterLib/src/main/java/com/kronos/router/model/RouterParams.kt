package com.kronos.router.model

/**
 * Created by zhangyang on 16/7/16.
 */
class RouterParams {
    var url: String? = null
    var weight: Int = 0
    var routerOptions: RouterOptions? = null
    var openParams: Map<String, String>? = null

    val realPath: String
        get() {
            try {
                return url?.let { it.subSequence(1, it.length).toString() } ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

}
