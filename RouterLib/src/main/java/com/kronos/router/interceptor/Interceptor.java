package com.kronos.router.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterParams;

import java.util.Map;

public interface Interceptor {
    void intercept(Chain chain) throws RouteNotFoundException;

    interface Chain {

        String url();

        Context getContext();

        Bundle getBundle();

        Map<String, HostParams> getHostParams();

        void proceed() throws RouteNotFoundException;

    }
}
