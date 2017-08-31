package com.less.tplayer.util;

import android.util.Log;

public class LogUtils {

    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────\n";
    private static String sTag = "wl";
    private static boolean debug = true;

    public void e(String msg) {
        if (debug) {
            setUpHeader();
            setUpContent(msg);
            setUpFooter();
        }
    }

    private void setUpHeader() {
        Log.e(sTag, SINGLE_DIVIDER);
    }

    private void setUpFooter() {
        Log.e(sTag, SINGLE_DIVIDER);
    }

    //╔ ╗
    public void setUpContent(String content) {
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();  //定位具体调用的方法
        Log.e(sTag, "(" + targetStackTraceElement.getFileName() + ":" + targetStackTraceElement.getLineNumber() + ")");
        Log.e(sTag, content);
    }

    private StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(LogUtils.class.getName()); //因为我们的入口是L类的方法，所以，我们直接遍历，L类相关的下一个非L类的栈帧信息就是具体调用的方法。
            if (shouldTrace && !isLogMethod) { // 定位到L类的方法时 ,shouldTrace仍为 false
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod; // 定位到L类的方法时然后执行到这里 ,shouldTrace此时为true ，为的是再遍历一次到下一个StackTraceElement才是我们需要的
        }
        return targetStackTrace;
    }
}
