package com.less.security;

/**
 * Created by deeper on 2017/10/23.
 */

public class Security {
    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();
}
