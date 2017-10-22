package com.less.tplayer;

import android.util.Log;

import com.less.tplayer.util.CrashHandler;

public class TpCrashHandler extends CrashHandler {

    private final static String TAG = TpCrashHandler.class.getSimpleName();

    private static CrashHandler crashHandler;

    public TpCrashHandler() {
        super();
    }

    public static void init() {
        crashHandler = new TpCrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    protected void onCrash() {
        Log.d(TAG,"success report crash");
    }
}
