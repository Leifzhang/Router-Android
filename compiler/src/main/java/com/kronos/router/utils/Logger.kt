package com.kronos.router.utils

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

class Logger(private val msg: Messager) {
    private val Logger = "RouterLogger:"

    /**
     * Print info log.
     */
    fun info(info: CharSequence) {
        msg.printMessage(Diagnostic.Kind.NOTE, Logger + info)

    }

    fun error(error: CharSequence) {
        msg.printMessage(Diagnostic.Kind.ERROR, Logger + "An exception is encountered, [" + error + "]")

    }

    fun error(error: Throwable?) {
        if (null != error) {
            msg.printMessage(Diagnostic.Kind.ERROR, Logger + "An exception is encountered, [" + error.message + "]" + "\n" + formatStackTrace(error.stackTrace))
        }
    }

    fun warning(warning: CharSequence) {
        msg.printMessage(Diagnostic.Kind.WARNING, Logger + warning)
    }

    private fun formatStackTrace(stackTrace: Array<StackTraceElement>): String {
        val sb = StringBuilder()
        for (element in stackTrace) {
            sb.append("    at ").append(element.toString())
            sb.append("\n")
        }
        return sb.toString()
    }

}
