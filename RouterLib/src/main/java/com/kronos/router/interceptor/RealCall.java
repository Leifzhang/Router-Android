package com.kronos.router.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealCall {

    private final Map<String, HostParams> hostMap;

    public RealCall(Map<String, HostParams> hostMap) {
        this.hostMap = hostMap;
    }

    public void open(String url, Context context, Bundle bundle) throws RouteNotFoundException {
        getParamsWithInterceptorChain(url, context, bundle);
    }

    private void getParamsWithInterceptorChain(String url, Context context, Bundle bundle) throws RouteNotFoundException {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new TestInterceptor());
        interceptors.add(new CacheInterceptor());
        interceptors.add(new RouterInterceptor(interceptors));
        Interceptor.Chain chain = new RealInterceptorChain(interceptors, url, hostMap, 0, context, bundle);
        chain.proceed();
    }

}
