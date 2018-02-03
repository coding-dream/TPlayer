package com.less.test.util;

/**
 * Created by deeper on 2018/2/3.
 */

public class ThreadUtil {

    public static void newThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
