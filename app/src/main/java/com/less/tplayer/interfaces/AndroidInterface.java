package com.less.tplayer.interfaces;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by deeper on 2017/12/30.
 */

public class AndroidInterface {

    private Handler deliver = new Handler(Looper.getMainLooper());
    private Context context;

    public AndroidInterface( Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void callAndroid(final String msg) {

        deliver.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context.getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}