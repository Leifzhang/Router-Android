package com.kronos.sample;

import com.kronos.router.Router;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouterConstants {
    public static void init() {
        Router.sharedRouter().map("https://github.com/leifzhang", TestActivity.class);
        Router.sharedRouter().map("https://wwww.baidu.com/:string", TestActivity.class);
    }
}
