package com.less.tplayer.http.callback;

/**
 * Created by deeper on 2017/11/20.
 */

public abstract class AbsCallback<T> implements HttpCallback<T> {

    @Override
    public void onStart() {
        throw new RuntimeException("not implement");
    }

    @Override
    public abstract void onSuccess(T response);

    @Override
    public abstract void onError(Exception e) ;

    @Override
    public void uploadProgress(float progress, long total) {
        throw new RuntimeException("not implement");
    }

    @Override
    public void onFinish() {
        throw new RuntimeException("not implement");
    }
}
