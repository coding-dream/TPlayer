package com.less.tplayer.api.callback;

import okhttp3.Response;

/**
 * Created by deeper on 2017/11/20.
 */

public abstract class AbsCallback<T> implements HttpCallback<T> {

    @Override
    public void onStart() {

    }

    @Override
    public abstract void onSuccess(Response response);

    @Override
    public abstract void onError(Exception e) ;

    @Override
    public void onFinish() {

    }
}
