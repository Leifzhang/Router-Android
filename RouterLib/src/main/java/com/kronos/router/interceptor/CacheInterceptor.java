package com.kronos.router.interceptor;

import android.util.Log;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.RouterParams;

import java.util.Map;

public class CacheInterceptor implements Interceptor {
    private final Map<String, RouterParams> cachedRoutes;

    CacheInterceptor(Map<String, RouterParams> cachedRoutes) {
        this.cachedRoutes = cachedRoutes;
    }

    @Override
    public RouterParams intercept(Chain chain) throws RouteNotFoundException {
        String url = chain.url();
        Log.i("TestInterceptor", "缓存池");
        if (cachedRoutes.get(url) != null) {
            return cachedRoutes.get(url);
        }
        RouterParams routerParams = chain.proceed(url);
        cachedRoutes.put(url, routerParams);
        return routerParams;
    }
}
