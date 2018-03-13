package com.kronos.router.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                String parser = parseUnit(key);
                key = key.replace(parser, "");
                Log.i("RouterUtils", parser);
                if (TextUtils.equals("{string}", parser)) {
                    formatParams.put(key, givenPart);
                    continue;
                } else {
                    Long.parseLong(givenPart);
                    formatParams.put(key, givenPart);
                }
                continue;
            }

            if (!routerPart.equals(givenPart)) {
                return null;
            }
        }
        return formatParams;
    }


    private static String parseUnit(String key) {
        Pattern p = Pattern.compile("\\{(.*)\\}");
        Matcher matcher = p.matcher(key);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "{long}";
    }

}
