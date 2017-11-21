package com.less.tplayer.http.callback;

import com.less.tplayer.http.convert.Converter;

/**
 * Created by deeper on 2017/11/20.
 */

public interface HttpCallback<T> extends Converter<T> {
    void onStart();
    void onSuccess(T response);
    void uploadProgress(float progress,long total);
    void onError(Exception e);
    void onFinish();
}
