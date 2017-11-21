package com.less.tplayer.http.convert;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by deeper on 2017/11/20.
 */

public class StringConvert implements Converter<String> {

    @Override
    public String convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        return body.string();
    }
}
