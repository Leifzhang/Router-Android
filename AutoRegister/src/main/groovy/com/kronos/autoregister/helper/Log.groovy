package com.kronos.autoregister.helper

class Log {
    def static info(Object msg) {
        try {
            println "${msg}"
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}