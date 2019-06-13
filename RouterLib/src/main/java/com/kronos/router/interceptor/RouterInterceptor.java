package com.kronos.router.interceptor;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterOptions;
import com.kronos.router.model.RouterParams;
import com.kronos.router.utils.RouterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RouterInterceptor implements Interceptor {

    @Override
    public RouterParams intercept(Chain chain) throws RouteNotFoundException {
        return getParams(chain.url(), chain.getCacheRouter(), chain.getHostParams());
    }

    private RouterParams getParams(String url, Map<String, RouterParams> _cachedRoutes, Map<String, HostParams> hosts) {
        Uri parsedUri = Uri.parse(url);
        String urlPath = TextUtils.isEmpty(parsedUri.getPath()) ? "" : parsedUri.
                getPath().substring(1);
        if (_cachedRoutes.get(url) != null) {
            return _cachedRoutes.get(url);
        }

        String[] givenParts = urlPath.split("/");
        List<RouterParams> params = new ArrayList<>();
        HostParams hostParams = hosts.get(parsedUri.getHost());
        if (hostParams == null) {
            throw new RouteNotFoundException("No host found for url " + url);
        }
        for (Map.Entry<String, RouterOptions> entry : hostParams.getRoutes().entrySet()) {
            RouterParams routerParams = getRouterParams(entry, givenParts);
            if (routerParams != null) {
                params.add(routerParams);
            }
        }

        RouterParams routerParams = params.size() == 1 ? params.get(0) : null;
        if (params.size() > 1) {
            for (RouterParams param : params) {
                if (TextUtils.equals(param.getRealPath(), urlPath)) {
                    routerParams = param;
                    break;
                }
            }
            if (routerParams == null) {
                Collections.sort(params, new Comparator<RouterParams>() {
                    @Override
                    public int compare(RouterParams o1, RouterParams o2) {
                        return o1.getWeight().compareTo(o2.getWeight());
                    }
                });
                routerParams = params.get(0);
            }
        }
        if (routerParams == null) {
            throw new RouteNotFoundException("No params found for url " + url);
        }
        for (String key : parsedUri.getQueryParameterNames()) {
            routerParams.getOpenParams().put(key, parsedUri.getQueryParameter(key));
        }
        routerParams.getOpenParams().put("targetUrl", url);
        _cachedRoutes.put(url, routerParams);

        Log.i("TestInterceptor", "真实处理的地方");
        return routerParams;
    }

    private RouterParams getRouterParams(Map.Entry<String, RouterOptions> entry, String[] givenParts) {
        RouterParams routerParams;
        String routerUrl = cleanUrl(entry.getKey());
        RouterOptions routerOptions = entry.getValue();
        String[] routerParts = routerUrl.split("/");
        if (routerParts.length != givenParts.length) {
            return null;
        }
        Map<String, String> givenParams = null;
        try {
            givenParams = RouterUtils.urlToParamsMap(givenParts, routerParts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (givenParams == null) {
            return null;
        }
        routerParams = new RouterParams();
        routerParams.setUrl(entry.getKey());
        routerParams.setWeight(entry.getValue().getWeight());
        routerParams.setOpenParams(givenParams);
        routerParams.setRouterOptions(routerOptions);
        return routerParams;
    }

    private String cleanUrl(String url) {
        if (url.startsWith("/")) {
            return url.substring(1);
        }
        return url;
    }
}
