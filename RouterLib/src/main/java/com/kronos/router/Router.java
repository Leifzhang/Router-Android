package com.kronos.router;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.kronos.router.exception.ContextNotProvided;
import com.kronos.router.exception.RouteNotFoundException;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterOptions;
import com.kronos.router.model.RouterParams;
import com.kronos.router.utils.RouterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Router {

    private static Router _router;

    public static Router sharedRouter() {  // 第一次检查
        if (_router == null) {
            synchronized (Router.class) {
                // 第二次检查
                if (_router == null) {
                    _router = new Router();
                }
            }
        }
        return _router;
    }

    private final Map<String, RouterParams> _cachedRoutes = new HashMap<String, RouterParams>();
    private Context _context;
    private final Map<String, HostParams> hosts = new HashMap<>();

    private Router() {

    }

    private Router(Context context) {
        this.setContext(context);
    }


    public void setContext(Context context) {
        this._context = context;
    }

    public Context getContext() {
        return this._context;
    }

    public void map(String url, RouterCallback callback) {
        RouterOptions options = new RouterOptions();
        options.setCallback(callback);
        this.map(url, null, options);
    }

    public void map(String url, Class<? extends Activity> klass) {
        this.map(url, klass, null);
    }

    public void map(String url, Class<? extends Activity> klass, RouterOptions options) {
        if (options == null) {
            options = new RouterOptions();
        }
        Uri uri = Uri.parse(url);
        options.setOpenClass(klass);
        HostParams hostParams;
        if (hosts.containsKey(uri.getHost())) {
            hostParams = hosts.get(uri.getHost());
        } else {
            hostParams = new HostParams(uri.getHost());
            hosts.put(hostParams.getHost(), hostParams);
        }
        hostParams.setRoute(uri.getPath(), options);
    }


    public void openExternal(String url) {
        this.openExternal(url, this._context);
    }


    public void openExternal(String url, Context context) {
        this.openExternal(url, null, context);
    }

    public void openExternal(String url, Bundle extras) {
        this.openExternal(url, extras, this._context);
    }


    public void openExternal(String url, Bundle extras, Context context) {
        if (context == null) {
            throw new ContextNotProvided(
                    "You need to supply a context for Router "
                            + this.toString());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.addFlagsToIntent(intent, context);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    public void open(String url) {
        this.open(url, this._context);
    }

    public void open(String url, Bundle extras) {
        this.open(url, extras, this._context);
    }

    public void open(String url, Context context) {
        this.open(url, null, context);
    }

    public void open(String url, Bundle extras, Context context) {
        if (context == null) {
            throw new ContextNotProvided(
                    "You need to supply a context for Router "
                            + this.toString());
        }
        RouterParams params = this.paramsForUrl(url);
        RouterOptions options = params.routerOptions;
        if (options.getCallback() != null) {
            RouterContext routeContext = new RouterContext(params.openParams, extras, context);

            options.getCallback().run(routeContext);
            return;
        }


        Intent intent = this.intentFor(context, params);
        if (intent == null) {
            // Means the options weren't opening a new activity
            return;
        }
        if (extras != null) {
            /* 路由进入 */
            intent.putExtras(extras);
        } else {
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
        }
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /*
     * Allows Intents to be spawned regardless of what context they were opened with.
     */
    private void addFlagsToIntent(Intent intent, Context context) {
        if (context == this._context) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }


    public Intent intentFor(String url) {
        RouterParams params = this.paramsForUrl(url);

        return intentFor(params);
    }

    private Intent intentFor(RouterParams params) {
        RouterOptions options = params.routerOptions;
        Intent intent = new Intent();
        if (options.getDefaultParams() != null) {
            intent.putExtras(options.getDefaultParams());
        }
        for (Entry<String, String> entry : params.openParams.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return intent;
    }


    public boolean isCallbackUrl(String url) {
        RouterParams params = this.paramsForUrl(url);
        RouterOptions options = params.routerOptions;
        return options.getCallback() != null;
    }


    public Intent intentFor(Context context, String url) {
        RouterParams params = this.paramsForUrl(url);

        return intentFor(context, params);
    }

    private Intent intentFor(Context context, RouterParams params) {
        RouterOptions options = params.routerOptions;
        if (options.getCallback() != null) {
            return null;
        }

        Intent intent = intentFor(params);
        intent.setClass(context, options.getOpenClass());
        this.addFlagsToIntent(intent, context);
        return intent;
    }


    private RouterParams paramsForUrl(String url) {
        Uri parsedUri = Uri.parse(url);

        String urlPath = parsedUri.getPath().substring(1);

        if (this._cachedRoutes.get(url) != null) {
            return this._cachedRoutes.get(url);
        }

        String[] givenParts = urlPath.split("/");
        List<RouterParams> params = new ArrayList<>();
        HostParams hostParams = hosts.get(parsedUri.getHost());
        for (Entry<String, RouterOptions> entry : hostParams.getRoutes().entrySet()) {
            RouterParams routerParams = null;
            String routerUrl = cleanUrl(entry.getKey());
            RouterOptions routerOptions = entry.getValue();
            String[] routerParts = routerUrl.split("/");

            if (routerParts.length != givenParts.length) {
                continue;
            }

            Map<String, String> givenParams = RouterUtils.urlToParamsMap(givenParts, routerParts);
            if (givenParams == null) {
                continue;
            }

            routerParams = new RouterParams();
            routerParams.url = entry.getKey();
            routerParams.openParams = givenParams;
            routerParams.routerOptions = routerOptions;
            params.add(routerParams);
        }

        RouterParams routerParams = params.size() == 1 ? params.get(0) : null;
        if (params.size() > 1) {
            for (RouterParams param : params) {
                if (TextUtils.equals(param.url, urlPath)) {
                    routerParams = param;
                    break;
                }
            }
        }
        if (routerParams == null) {
            throw new RouteNotFoundException("No route found for url " + url);
        }
        for (String key : parsedUri.getQueryParameterNames()) {
            routerParams.openParams.put(key, parsedUri.getQueryParameter(key));
        }

        this._cachedRoutes.put(url, routerParams);
        return routerParams;
    }


    private String cleanUrl(String url) {
        if (url.startsWith("/")) {
            return url.substring(1, url.length());
        }
        return url;
    }


}
