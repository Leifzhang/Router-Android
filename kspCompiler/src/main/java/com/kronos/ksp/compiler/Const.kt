package com.kronos.ksp.compiler

object Const {
    const val KEY_MODULE_NAME = "ROUTER_MODULE_NAME"
    const val DEFAULT_APP_MODULE = "main_app"


    // System interface
    const val ACTIVITY = "android.app.Activity"
    const val FRAGMENT = "android.app.Fragment"
    const val FRAGMENT_V4 = "android.support.v4.app.Fragment"
    const val SERVICE = "android.app.Service"
    const val PARCELABLE = "android.os.Parcelable"
    const val RUNNABLE = "com.kronos.router.RouterCallback"
    const val INTERCEPTOR_CLASS = "com.kronos.router.interceptor.Interceptor"

    // Java type
    private const val LANG = "java.lang"
    const val BYTE = "$LANG.Byte"
    const val SHORT = "$LANG.Short"
    const val INTEGER = "$LANG.Integer"
    const val LONG = "$LANG.Long"
    const val FLOAT = "$LANG.Float"
    const val DOUBLE = "$LANG.Double"
    const val BOOLEAN = "$LANG.Boolean"
    const val CHAR = "$LANG.Character"
    const val STRING = "$LANG.String"
    const val SERIALIZABLE = "java.io.Serializable"

}