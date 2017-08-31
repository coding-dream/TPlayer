package com.less.tplayer;


import android.app.Application;

/**
 * Created by Administrator on 2017/8/7.
 */

public class TpApplication extends Application {
    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static TpApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return AppContext
     */
    public static TpApplication getContext() {
        return instance;
    }

}
