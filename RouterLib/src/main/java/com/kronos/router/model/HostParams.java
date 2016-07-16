package com.kronos.router.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang on 16/7/16.
 */
public class HostParams {
    private String host;

    public HostParams(String host) {
        this.host = host;
    }

    private final Map<String, RouterOptions> _routes = new HashMap<String, RouterOptions>();

    public void setRoute(String path, RouterOptions options) {
        _routes.put(path, options);
    }

    public RouterOptions getOptions(String path) {
        return _routes.get(path);
    }

    public Map<String, RouterOptions> getRoutes() {
        return _routes;
    }

    public String getHost() {
        return host;
    }
}
