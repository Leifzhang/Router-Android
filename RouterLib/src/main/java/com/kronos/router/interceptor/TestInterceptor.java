package com.kronos.router.interceptor;

import android.util.Log;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.RouterParams;

public class TestInterceptor implements Interceptor {
    @Override
    public RouterParams intercept(Chain chain) throws RouteNotFoundException {
        String url = chain.url();
        Log.i("TestInterceptor", "准备处理请求:" + url);
        RouterParams params = chain.proceed(url);
        Log.i("TestInterceptor", "获取路由结果:" + params.getRouterOptions().getOpenClass().getCanonicalName());
        return params;
    }
}
