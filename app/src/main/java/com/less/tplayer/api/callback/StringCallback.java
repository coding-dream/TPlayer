package com.less.tplayer.api.callback;

import com.less.tplayer.api.convert.StringConvert;

import okhttp3.Response;

/**
 * Created by deeper on 2017/11/20.
 */

public abstract class StringCallback extends AbsCallback<String> {
    private StringConvert convert;

    public StringCallback() {
        convert = new StringConvert();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        return convert.convertResponse(response);
    }
}
