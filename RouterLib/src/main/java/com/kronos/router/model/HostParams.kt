package com.kronos.router.model

import java.util.HashMap

/**
 * Created by zhangyang on 16/7/16.
 */
class HostParams(val host: String) {
    private val _routes = HashMap<String, RouterOptions>()

    val routes: Map<String, RouterOptions>
        get() = _routes

    fun setRoute(path: String, options: RouterOptions) {
        if (routes.containsKey(path)) {
            val oldOptions = _routes[path]
            if (oldOptions!!.weight < options.weight) {
                _routes.put(path, options)
            }
        } else {
            _routes.put(path, options)
        }
    }

    fun getOptions(path: String): RouterOptions? {
        return _routes[path]
    }
}
