package com.less.tplayer.api.callback;

import com.less.tplayer.api.convert.Converter;

import okhttp3.Response;

/**
 * Created by deeper on 2017/11/20.
 */

public interface HttpCallback<T> extends Converter<T> {
    void onStart();
    void onSuccess(Response response);
    void onError(Exception e);
    void onFinish();
}
