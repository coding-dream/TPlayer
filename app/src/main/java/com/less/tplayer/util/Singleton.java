package com.less.tplayer.util;

/**
 * Android 源码中的单例写法
 * @author deeper
 * @date 2017/11/18
 */

public abstract class Singleton<T> {
    private T mInstance;
    protected abstract T create();
    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }
}
