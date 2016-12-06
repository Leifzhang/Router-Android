package com.kronos.router;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Leif Zhang on 2016/12/5.
 * Email leifzhanggithub@gmail.com
 */
@Retention(RetentionPolicy.CLASS)
public @interface BindModule {
    String value();
}
