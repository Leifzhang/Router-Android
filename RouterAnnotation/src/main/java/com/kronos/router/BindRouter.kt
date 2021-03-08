package com.kronos.router

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * Created by Leif Zhang on 2016/12/2.
 * Email leifzhanggithub@gmail.com
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.CLASS)
annotation class BindRouter(val urls: Array<String>, val weight: Int = 0, val interceptors: Array<KClass<*>> = []) {

}