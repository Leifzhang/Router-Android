package com.kronos.router;

import java.lang.reflect.Method;

/**
 * Created by Leif Zhang on 2016/12/6.
 * Email leifzhanggithub@gmail.com
 */

public class RouterBind {
    public static void bind(String className) {
        try {
            Class routerInit = Class.forName(className);
            Method method = routerInit.getMethod("init");
            method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
