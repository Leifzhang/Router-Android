package com.kronos.router.model;

import android.app.Activity;

import com.kronos.router.RouterCallback;

import java.util.Map;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterOptions {
    Class<? extends Activity> _activityClass;
    RouterCallback _callback;
    Map<String, String> _defaultParams;

    public RouterOptions() {

    }

    public RouterOptions(Class<? extends Activity> klass) {
        this.setOpenClass(klass);
    }

    public RouterOptions(Map<String, String> defaultParams) {
        this.setDefaultParams(defaultParams);
    }

    public RouterOptions(Map<String, String> defaultParams, Class<? extends Activity> klass) {
        this.setDefaultParams(defaultParams);
        this.setOpenClass(klass);
    }

    public void setOpenClass(Class<? extends Activity> klass) {
        this._activityClass = klass;
    }

    public Class<? extends Activity> getOpenClass() {
        return this._activityClass;
    }

    public RouterCallback getCallback() {
        return this._callback;
    }

    public void setCallback(RouterCallback callback) {
        this._callback = callback;
    }

    public void setDefaultParams(Map<String, String> defaultParams) {
        this._defaultParams = defaultParams;
    }

    public Map<String, String> getDefaultParams() {
        return this._defaultParams;
    }
}
