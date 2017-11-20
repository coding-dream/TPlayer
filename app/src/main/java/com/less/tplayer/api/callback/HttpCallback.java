package com.less.tplayer.api.callback;

import com.less.tplayer.api.convert.Converter;

/**
 * Created by deeper on 2017/11/20.
 */

public interface HttpCallback<T> extends Converter<T> {
    void onStart();
    void onSuccess(T response);
    void onError(Exception e);
    void onFinish();
}
