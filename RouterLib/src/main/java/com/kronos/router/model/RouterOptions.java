package com.kronos.router.model;

import android.app.Activity;
import android.os.Bundle;

import com.kronos.router.RouterCallback;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterOptions {
    Class<? extends Activity> _activityClass;
    RouterCallback _callback;
    Bundle _defaultParams;

    public RouterOptions() {

    }

    public RouterOptions(Class<? extends Activity> klass) {
        this.setOpenClass(klass);
    }

    public RouterOptions(Bundle defaultParams) {
        this.setDefaultParams(defaultParams);
    }

    public RouterOptions(Bundle defaultParams, Class<? extends Activity> klass) {
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

    public void setDefaultParams(Bundle defaultParams) {
        this._defaultParams = defaultParams;
    }

    public Bundle getDefaultParams() {
        return this._defaultParams;
    }
}
