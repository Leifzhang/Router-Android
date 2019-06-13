package com.kronos.router.interceptor;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealCall {

    private Map<String, RouterParams> cachedRoutes = new HashMap<>();
    private final Map<String, HostParams> hostMap;

    public RealCall(Map<String, HostParams> hostMap) {
        this.hostMap = hostMap;
    }

    public RouterParams open(String url) throws RouteNotFoundException {
        return getParamsWithInterceptorChain(url);
    }

    private RouterParams getParamsWithInterceptorChain(String url) throws RouteNotFoundException {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new TestInterceptor());
        interceptors.add(new RouterInterceptor());
        Interceptor.Chain chain = new RealInterceptorChain(interceptors, url, cachedRoutes, hostMap, 0);
        return chain.proceed(url);
    }

}
