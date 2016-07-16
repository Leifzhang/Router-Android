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

    /**
     * A globally accessible Router instance that will work for
     * most use cases.
     */
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


    private String _rootUrl = null;
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

    /**
     * @return The context for the router
     */
    public Context getContext() {
        return this._context;
    }

    /**
     * Map a URL to a callback
     *
     * @param url      The URL being mapped; for example, "users/:id" or "groups/:id/topics/:topic_id"
     * @param callback {@link RouterCallback} instance which contains the code to execute when the URL is opened
     */
    public void map(String url, RouterCallback callback) {
        RouterOptions options = new RouterOptions();
        options.setCallback(callback);
        this.map(url, null, options);
    }

    /**
     * Map a URL to open an {@link Activity}
     *
     * @param url   The URL being mapped; for example, "users/:id" or "groups/:id/topics/:topic_id"
     * @param klass The {@link Activity} class to be opened with the URL
     */
    public void map(String url, Class<? extends Activity> klass) {
        this.map(url, klass, null);
    }

    /**
     * Map a URL to open an {@link Activity}
     *
     * @param url     The URL being mapped; for example, "users/:id" or "groups/:id/topics/:topic_id"
     * @param klass   The {@link Activity} class to be opened with the URL
     * @param options The {@link RouterOptions} to be used for more granular and customized options for when the URL is opened
     */
    public void map(String url, Class<? extends Activity> klass, RouterOptions options) {
        if (options == null) {
            options = new RouterOptions();
        }
        Uri uri = Uri.parse(url);
        options.setOpenClass(klass);
        HostParams hostParams;
        if (hosts.containsKey(uri.getHost())) {
            hostParams = hosts.get(uri.getPath());
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

    /**
     * Open a map'd URL set using {@link #map(String, Class)} or {@link #map(String, RouterCallback)}
     *
     * @param url The URL; for example, "users/16" or "groups/5/topics/20"
     */
    public void open(String url) {
        this.open(url, this._context);
    }

    /**
     * Open a map'd URL set using {@link #map(String, Class)} or {@link #map(String, RouterCallback)}
     *
     * @param url    The URL; for example, "users/16" or "groups/5/topics/20"
     * @param extras The {@link Bundle} which contains the extras to be assigned to the generated {@link Intent}
     */
    public void open(String url, Bundle extras) {
        this.open(url, extras, this._context);
    }

    /**
     * Open a map'd URL set using {@link #map(String, Class)} or {@link #map(String, RouterCallback)}
     *
     * @param url     The URL; for example, "users/16" or "groups/5/topics/20"
     * @param context The context which is used in the generated {@link Intent}
     */
    public void open(String url, Context context) {
        this.open(url, null, context);
    }

    /**
     * Open a map'd URL set using {@link #map(String, Class)} or {@link #map(String, RouterCallback)}
     *
     * @param url     The URL; for example, "users/16" or "groups/5/topics/20"
     * @param extras  The {@link Bundle} which contains the extras to be assigned to the generated {@link Intent}
     * @param context The context which is used in the generated {@link Intent}
     */
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

    /**
     * @param url The URL; for example, "users/16" or "groups/5/topics/20"
     * @return The {@link Intent} for the url
     */
    public Intent intentFor(String url) {
        RouterParams params = this.paramsForUrl(url);

        return intentFor(params);
    }

    private Intent intentFor(RouterParams params) {
        RouterOptions options = params.routerOptions;
        Intent intent = new Intent();
        if (options.getDefaultParams() != null) {
            for (Entry<String, String> entry : options.getDefaultParams().entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        for (Entry<String, String> entry : params.openParams.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return intent;
    }

    /**
     * @param url The URL to check
     * @return Whether or not the URL refers to an anonymous callback function
     */
    public boolean isCallbackUrl(String url) {
        RouterParams params = this.paramsForUrl(url);
        RouterOptions options = params.routerOptions;
        return options.getCallback() != null;
    }

    /**
     * @param context The context which is spawning the intent
     * @param url     The URL; for example, "users/16" or "groups/5/topics/20"
     * @return The {@link Intent} for the url, with the correct {@link Activity} set, or null.
     */
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

    /*
     * Takes a url (i.e. "/users/16/hello") and breaks it into a {@link RouterParams} instance where
     * each of the parameters (like ":id") has been parsed.
     */
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


    /**
     * Clean up url
     *
     * @param url
     * @return cleaned url
     */
    private String cleanUrl(String url) {
        if (url.startsWith("/")) {
            return url.substring(1, url.length());
        }
        return url;
    }


}
