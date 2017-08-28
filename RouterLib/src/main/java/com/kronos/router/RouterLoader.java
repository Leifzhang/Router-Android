package com.kronos.router;

import android.app.Application;

import com.kronos.router.utils.ClassUtils;

import java.util.List;

/**
 * Created by  Leif Zhang on 2017/8/28.
 * Email leifzhanggithub@gmail.com
 */

class RouterLoader implements Runnable {

    private final String RouterPathPackage = "com.kronos.router.init";
    private Application app;
    private Thread loaderThread;

    RouterLoader() {
        loaderThread = new Thread(this);
    }

    void attach(Application context) {
        this.app = context;
        loaderThread.start();
    }

    @Override
    public void run() {
        try {
            List<String> classFileNames = ClassUtils.getFileNameByPackageName(app, RouterPathPackage);
            for (String className : classFileNames) {
                RouterBind.bind(className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
