package com.kronos.router.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterUtils {

    public static Map<String, String> urlToParamsMap(String[] givenUrlSegments, String[] routerUrlSegments) throws Exception {
        Map<String, String> formatParams = new HashMap<>();
        for (int index = 0; index < routerUrlSegments.length; index++) {
            String routerPart = routerUrlSegments[index];
            String givenPart = givenUrlSegments[index];
            if (TextUtils.equals(routerPart, givenPart)) {
                continue;
            }
            if (routerPart.charAt(0) == ':') {
                String key = routerPart.substring(1, routerPart.length());
                if (TextUtils.equals(":string", routerPart)) {
                    formatParams.put(key, givenPart);
                    continue;
                }
                Long.parseLong(givenPart);
                formatParams.put(key, givenPart);
                continue;
            }

            if (!routerPart.equals(givenPart)) {
                return null;
            }
        }

        return formatParams;
    }

}
