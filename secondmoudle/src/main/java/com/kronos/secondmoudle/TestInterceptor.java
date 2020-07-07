package com.kronos.secondmoudle;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.interceptor.Interceptor;

import org.jetbrains.annotations.NotNull;

public class TestInterceptor implements Interceptor {
    @Override
    public void intercept(@NotNull Chain chain) throws RouteNotFoundException {
        chain.proceed();
    }
}
