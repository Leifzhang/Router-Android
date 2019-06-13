package com.kronos.router.interceptor;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import java.util.Map;

public interface Interceptor {
    RouterParams intercept(Chain chain) throws RouteNotFoundException;

    interface Chain {
        String url();

        RouterParams proceed(String url) throws RouteNotFoundException;

        Map<String, RouterParams> getCacheRouter();

        Map<String, HostParams> getHostParams();
    }
}
