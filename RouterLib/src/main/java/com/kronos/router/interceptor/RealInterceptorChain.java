package com.kronos.router.interceptor;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import java.util.List;
import java.util.Map;

public class RealInterceptorChain implements Interceptor.Chain {
    private final List<Interceptor> interceptors;
    private final String url;
    private final Map<String, RouterParams> cachedRoutes;
    private final Map<String, HostParams> hostMap;
    private final int index;


    RealInterceptorChain(List<Interceptor> interceptors, String url, Map<String, RouterParams> cachedRoutes,
                         Map<String, HostParams> hosts, int index) {
        this.interceptors = interceptors;
        this.url = url;
        this.cachedRoutes = cachedRoutes;
        this.hostMap = hosts;
        this.index = index;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Map<String, RouterParams> getCacheRouter() {
        return cachedRoutes;
    }

    @Override
    public Map<String, HostParams> getHostParams() {
        return hostMap;
    }

    @Override
    public RouterParams proceed(String url) throws RouteNotFoundException {
        return proceed(url, "");
    }

    public RouterParams proceed(String request, String test) throws RouteNotFoundException {
        if (index >= interceptors.size()) throw new AssertionError();

        // Call the next intercept in the chain.
        RealInterceptorChain next = new RealInterceptorChain(interceptors, request, cachedRoutes, hostMap,
                index + 1);
        Interceptor interceptor = interceptors.get(index);
        RouterParams response = interceptor.intercept(next);


        // Confirm that the intercepted response isn't null.
        if (response == null) {
            throw new RouteNotFoundException("No route found for url " + url);
        }

        return response;
    }
}
