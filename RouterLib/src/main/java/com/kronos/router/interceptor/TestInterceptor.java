package com.kronos.router.interceptor;

import android.util.Log;

import com.kronos.router.exception.RouteNotFoundException;

public class TestInterceptor implements Interceptor {
    @Override
    public void intercept(Chain chain) throws RouteNotFoundException {
        Log.i("TestInterceptor", "TestInterceptor");
        chain.proceed();
    }
}
