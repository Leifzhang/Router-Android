package com.kronos.router.interceptor;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RealCall {

    private final String url;
    private final Map<String, RouterParams> cachedRoutes;
    private final Map<String, HostParams> hostMap;

    public RealCall(String url, Map<String, RouterParams> cachedRoutes, Map<String, HostParams> hostMap) {
        this.url = url;
        this.cachedRoutes = cachedRoutes;
        this.hostMap = hostMap;
    }


    public RouterParams getParamsWithInterceptorChain() throws RouteNotFoundException {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new RouterInterceptor());
        Interceptor.Chain chain = new RealInterceptorChain(interceptors, url, cachedRoutes, hostMap, 0);
        return chain.proceed(url);
    }

}
