package com.kronos.router.exception;

/**
 * Created by zhangyang on 16/7/16.
 */
public class RouteNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2278644339983544651L;

    public RouteNotFoundException(String message) {
        super(message);
    }
}