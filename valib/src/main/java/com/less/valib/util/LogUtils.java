package com.less.valib.util;

import android.util.Log;

/**
 * @author Administrator
 */
public class LogUtils {
    private static final String TAG = LogUtils.class.getSimpleName();
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────\n";
    private static boolean debug = true;

    public static void d(String msg) {
        if (debug) {
            setUpHeader();
            setUpContent(msg);
            setUpFooter();
        }
    }

    private static void setUpHeader() {
        Log.e(TAG, SINGLE_DIVIDER);
    }

    private static void setUpFooter() {
        Log.e(TAG, SINGLE_DIVIDER);
    }

    private static void setUpContent(String content) {
        //定位具体调用的方法
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.e(TAG, "(" + targetStackTraceElement.getFileName() + ":" + targetStackTraceElement.getLineNumber() + ")");
        Log.e(TAG, content);
    }

    private static StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            // 因为我们的入口是L类的方法，所以，我们直接遍历，L类相关的下一个非L类的栈帧信息就是具体调用的方法。
            boolean isLogMethod = stackTraceElement.getClassName().equals(LogUtils.class.getName());
            // 定位到L类的方法时 ,shouldTrace仍为 false
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            // 定位到L类的方法时然后执行到这里 ,shouldTrace此时为true ，为的是再遍历一次到下一个StackTraceElement才是我们需要的
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }
}