package com.less.plugin;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class Dog implements Animal {
    OkHttpClient okhttp = new OkHttpClient();

    @Override
    public void say(final Callback callback) {
        Builder builder = (new Builder()).url("http://www.baidu.com");
        Call call = this.okhttp.newCall(builder.build());
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.done("error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                callback.done(content);
            }
        });
    }
}
