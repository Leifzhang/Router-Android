package com.kronos.router.exception

/**
 * Created by zhangyang on 16/7/16.
 */
class RouteNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        private const val serialVersionUID = -2278644339983544651L
    }
}