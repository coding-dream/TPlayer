package com.less.test;

import android.app.Application;
import android.content.Context;

/**
 * Created by deeper on 2018/2/3.
 */

public class App extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext(){
        return instance;
    }
}
