package com.kronos.router;

import android.content.Context;
import android.os.Bundle;

import java.util.Map;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterContext {
    private Map<String, String> _params;
    private Bundle _extras;
    private Context _context;

    public RouterContext(Map<String, String> params, Bundle extras, Context context) {
        _params = params;
        _extras = extras;
        _context = context;
    }

    public Map<String, String> getParams() {
        return _params;
    }

    public Bundle getExtras() {
        return _extras;
    }

    public Context getContext() {
        return _context;
    }
}
