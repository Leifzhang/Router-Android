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
        if (_defaultParams == null) {
            _defaultParams = new Bundle();
        }
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
        if (_defaultParams == null) {
            _defaultParams = new Bundle();
        }
        if (defaultParams != null) {
            _defaultParams.putAll(defaultParams);
        }
    }


    public void putParams(String key, String value) {
        _defaultParams.putString(key, value);
    }

    public Bundle getDefaultParams() {
        return this._defaultParams;
    }
}
