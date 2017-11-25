package com.less.tplayer.mvp.common;

/**
 * Created by deeper on 2017/11/24.
 */

public class Injection {
    public static <T> T provideRepository(Class<T> repositoryClazz) {
        T t = null;
        try {
            t = repositoryClazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
