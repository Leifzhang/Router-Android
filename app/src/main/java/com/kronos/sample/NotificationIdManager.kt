package com.kronos.sample

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.core.app.NotificationManagerCompat

/**
 *
 *  @Author LiABao
 *  @Since 2021/10/14
 *
 */
class NotificationIdManager private constructor() {

    private val map = SparseIntArray()
    private val keyMap = SparseArray<HashSet<Int>>()

    fun createNotificationId(code: BUSINESS): Pair<String, Int> {
        val value = map[code.ordinal] + 1
        map.put(code.ordinal, value)
        return getIdBySize(code, value) to getIntBySize(code, value)
    }

    fun createNotificationIdByCustom(code: BUSINESS, hashCode: Int): Pair<String, Int> {
        val value = keyMap[code.ordinal] ?: hashSetOf()
        value.add(hashCode)
        keyMap.put(code.ordinal, value)
        return getIdBySize(code, hashCode) to getIntBySize(code, hashCode)
    }

    fun getNotificationId(code: BUSINESS): Pair<String, Int> {
        val value = map[code.ordinal]
        return getIdBySize(code, value) to getIntBySize(code, value)
    }

    fun getNotificationIdByCustom(code: BUSINESS, hashCode: Int): Pair<String, Int> {
        val value = keyMap[code.ordinal] ?: hashSetOf()
        value.add(hashCode)
        keyMap.put(code.ordinal, value)
        return getIdBySize(code, hashCode) to getIntBySize(code, hashCode)
    }


    fun getAllIdByBusiness(code: BUSINESS): MutableList<Pair<String, Int>> {
        val value = map[code.ordinal]
        val list = mutableListOf<Pair<String, Int>>()
        for (i in 0 until value) {
            list.add(getIdBySize(code, i + 1) to getIntBySize(code, i + 1))
        }
        return list
    }


    fun clearIdByBusiness(code: BUSINESS) {
        map.put(code.ordinal, 0)
    }

    private fun getIntBySize(code: BUSINESS, value: Int): Int {
        var newValue = ((code.ordinal + 1) * 1000 + value).toLong()
        if (newValue >= Int.MAX_VALUE) {
            newValue -= Int.MAX_VALUE
        }
        return newValue.toInt()
    }

    private fun getIdBySize(code: BUSINESS, value: Int): String {
        return "business_${code.name}_id_${value}"
    }

    companion object {

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NotificationIdManager()
        }

    }
}

enum class BUSINESS {
    MAIN, GAME, OGV
}


fun Notification.notify(context: Context, code: BUSINESS) {
    NotificationManagerCompat.from(context).let {
        val pair = NotificationIdManager.INSTANCE.createNotificationId(code)
        it.notify(pair.first, pair.second, this@notify)
    }
}

fun Notification.notify(context: Context, code: BUSINESS, custom: Int) {
    NotificationManagerCompat.from(context).let {
        val pair = NotificationIdManager.INSTANCE.createNotificationIdByCustom(code, custom)
        it.notify(pair.first, pair.second, this@notify)
    }
}


fun NotificationManager.cancel(code: BUSINESS) {
    val pair = NotificationIdManager.INSTANCE.getNotificationId(code)
    cancel(pair.first, pair.second)
}


fun NotificationManager.cancel(code: BUSINESS, custom: Int) {
    val pair = NotificationIdManager.INSTANCE.getNotificationIdByCustom(code, custom)
    cancel(pair.first, pair.second)
}


fun NotificationManager.cancelAll(code: BUSINESS) {
    NotificationIdManager.INSTANCE.getAllIdByBusiness(code).forEach {
        cancel(it.first, it.second)
    }
    NotificationIdManager.INSTANCE.clearIdByBusiness(code)
}