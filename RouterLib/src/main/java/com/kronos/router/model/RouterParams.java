package com.kronos.router.model;

import java.util.Map;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterParams {
    public String url;
    private int weight;
    public RouterOptions routerOptions;
    public Map<String, String> openParams;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
