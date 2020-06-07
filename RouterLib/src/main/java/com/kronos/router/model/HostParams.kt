package com.kronos.router.model

import java.util.HashMap

/**
 * Created by zhangyang on 16/7/16.
 */
class HostParams(val host: String) {
    val routes = HashMap<String, RouterOptions>()

    fun setRoute(path: String, options: RouterOptions) {
        if (routes.containsKey(path)) {
            val oldOptions = routes[path]
            if (oldOptions?.weight ?: 0 < options.weight) {
                routes[path] = options
            }
        } else {
            routes[path] = options
        }
    }

    fun getOptions(path: String): RouterOptions? {
        return routes[path]
    }
}
