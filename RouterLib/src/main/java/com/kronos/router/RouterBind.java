package com.kronos.router;

import java.lang.reflect.Method;

/**
 * Created by Leif Zhang on 2016/12/6.
 * Email leifzhanggithub@gmail.com
 */

public class RouterBind {
    public static void bind(Class t) {
        try {
            t.newInstance();
            BindModule bindModule = (BindModule) t.getAnnotation(BindModule.class);
            String value = bindModule.value();
            String className = String.format("com.kronos.router.init.RouterInit_%s", value);
            Class routerInit = Class.forName(className);
            Method method = routerInit.getMethod("init");
            method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
