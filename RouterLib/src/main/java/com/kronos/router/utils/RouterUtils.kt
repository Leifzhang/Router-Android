package com.kronos.router.utils

import android.text.TextUtils
import android.util.Log
import java.util.*
import java.util.regex.Pattern

/**
 * Created by zhangyang on 16/7/16.
 */
object RouterUtils {

    @Throws(Exception::class)
    fun urlToParamsMap(givenUrlSegments: Array<String>, routerUrlSegments: Array<String>): Map<String, String>? {
        val formatParams: MutableMap<String, String> = HashMap()
        for (index in routerUrlSegments.indices) {
            val routerPart = routerUrlSegments[index]
            val givenPart = givenUrlSegments[index]
            if (TextUtils.equals(routerPart, givenPart)) {
                continue
            }
            if (routerPart[0] == ':') {
                var key = routerPart.substring(1, routerPart.length)
                val parser = parseUnit(key)
                key = key.replace(parser, "")
                if (TextUtils.equals("{string}", parser)) {
                    formatParams[key] = givenPart
                    continue
                } else {
                    givenPart.toLong()
                    formatParams[key] = givenPart
                }
                continue
            }
            if (routerPart != givenPart) {
                return null
            }
        }
        return formatParams
    }

    private fun parseUnit(key: String): String {
        val p = Pattern.compile("\\{(.*)\\}")
        val matcher = p.matcher(key)
        return if (matcher.find()) {
            matcher.group(0)
        } else "{long}"
    }
}