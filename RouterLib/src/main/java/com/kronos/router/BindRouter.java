package com.kronos.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Leif Zhang on 2016/12/2.
 * Email leifzhanggithub@gmail.com
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface BindRouter {
    String[] urls();

    boolean isRunnable() default false;

    int weight() default 0;
}
