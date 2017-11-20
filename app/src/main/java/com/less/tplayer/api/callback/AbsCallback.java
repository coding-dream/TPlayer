package com.less.tplayer.api.callback;

/**
 * Created by deeper on 2017/11/20.
 */

public abstract class AbsCallback<T> implements HttpCallback<T> {

    @Override
    public void onStart() {

    }

    @Override
    public abstract void onSuccess(T response);

    @Override
    public abstract void onError(Exception e) ;

    @Override
    public void onFinish() {

    }
}
