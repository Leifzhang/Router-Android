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
    public void intercept(Chain chain) throws RouteNotFoundException {
        String url = chain.url();
        if (cachedRoutes.get(url) != null) {
           // return cachedRoutes.get(url);
        }

        chain.proceed();
      //  cachedRoutes.put(url, routerParams);

    }

}
