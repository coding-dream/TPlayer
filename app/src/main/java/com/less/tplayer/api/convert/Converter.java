package com.less.tplayer.api.convert;

import okhttp3.Response;

/**
 * Created by deeper on 2017/11/20.
 */

public interface Converter<T> {
    T convertResponse(Response response) throws Throwable;
}
