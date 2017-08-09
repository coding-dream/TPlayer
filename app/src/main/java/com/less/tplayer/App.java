package com.less.tplayer;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/8/7.
 */

public class App extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
