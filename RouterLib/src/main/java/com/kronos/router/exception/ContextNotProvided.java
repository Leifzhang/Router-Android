package com.kronos.router.exception;

/**
 * Created by zhangyang on 16/7/16.
 */
public class ContextNotProvided extends RuntimeException {
    private static final long serialVersionUID = -1381427067387547157L;

    public ContextNotProvided(String message) {
        super(message);
    }
}