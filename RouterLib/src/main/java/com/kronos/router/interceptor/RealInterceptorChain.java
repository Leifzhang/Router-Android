package com.kronos.router.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RealInterceptorChain implements Interceptor.Chain {

    private final List<Interceptor> interceptors;
    private final String url;
    private final Map<String, HostParams> hostMap;
    private final int index;
    private Context mContext;
    private Bundle mBundle;

    RealInterceptorChain(List<Interceptor> interceptors, String url,
                         Map<String, HostParams> hosts, int index, Context context, Bundle bundle) {
        this.interceptors = interceptors;
        this.url = url;
        this.hostMap = hosts;
        this.index = index;
        this.mContext = context;
        this.mBundle = bundle;
    }


    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Bundle getBundle() {
        return mBundle;
    }


    @Override
    public Map<String, HostParams> getHostParams() {
        return hostMap;
    }

    @Override
    public void proceed() throws RouteNotFoundException {
        proceed(url);
    }


    public void proceed(String request) throws RouteNotFoundException {
        if (index >= interceptors.size()) {
            return;
        }

        RealInterceptorChain next = new RealInterceptorChain(interceptors, request, hostMap,
                index + 1, mContext, mBundle);
        Interceptor interceptor = interceptors.get(index);
        interceptor.intercept(next);
    }

    @NotNull
    @Override
    public String getUrl() {
        return url;
    }
}
