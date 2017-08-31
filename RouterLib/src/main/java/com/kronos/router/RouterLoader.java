package com.kronos.router;

import android.app.Application;

import com.kronos.router.utils.ClassUtils;

import java.util.List;

/**
 * Created by  Leif Zhang on 2017/8/28.
 * Email leifzhanggithub@gmail.com
 */

class RouterLoader {

    private final String RouterPathPackage = "com.kronos.router.init";

    RouterLoader() {

    }

    void attach(Application context) {
        try {
            List<String> classFileNames = ClassUtils.getFileNameByPackageName(context, RouterPathPackage);
            for (String className : classFileNames) {
                RouterBind.bind(className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
