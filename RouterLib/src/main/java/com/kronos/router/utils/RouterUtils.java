package com.kronos.router.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterUtils {
    /**
     * @param givenUrlSegments  An array representing the URL path attempting to be opened (i.e. ["users", "42"])
     * @param routerUrlSegments An array representing a possible URL match for the router (i.e. ["users", ":id"])
     * @return A map of URL parameters if it's a match (i.e. {"id" => "42"}) or null if there is no match
     */
    public static Map<String, String> urlToParamsMap(String[] givenUrlSegments, String[] routerUrlSegments) {
        Map<String, String> formatParams = new HashMap<String, String>();
        for (int index = 0; index < routerUrlSegments.length; index++) {
            String routerPart = routerUrlSegments[index];
            String givenPart = givenUrlSegments[index];

            if (routerPart.charAt(0) == ':') {
                String key = routerPart.substring(1, routerPart.length());
                if (TextUtils.equals(":symbol", routerPart)) {
                    formatParams.put(key, givenPart);
                    continue;
                }
                try {
                    long i = Long.parseLong(givenPart);
                    formatParams.put(key, givenPart);
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            if (!routerPart.equals(givenPart)) {
                return null;
            }
        }

        return formatParams;
    }

}
