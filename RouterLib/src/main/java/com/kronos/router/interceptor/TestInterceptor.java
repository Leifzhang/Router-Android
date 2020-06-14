package com.kronos.router.interceptor;

import com.kronos.router.exception.RouteNotFoundException;

public class TestInterceptor implements Interceptor {
    @Override
    public void intercept(Chain chain) throws RouteNotFoundException {
        chain.proceed();
    }
}
