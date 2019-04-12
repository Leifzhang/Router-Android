package com.kronos.router.exception

/**
 * Created by zhangyang on 16/7/16.
 */
class ContextNotProvided(message: String) : RuntimeException(message) {
    companion object {
        private val serialVersionUID = -1381427067387547157L
    }
}