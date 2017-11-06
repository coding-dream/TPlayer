package com.less.tplayer.util;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.util.Date;

/**
 * 全局捕获异常类
 *
 * @author lzq
 */
public abstract class CrashHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler mDefaultHandler;

    protected CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * bug捕获
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    public boolean handleException(Throwable ex) {
        if (ex == null)
            return false;
        String crashReport = getCrashReport(ex);
        Log.e("ex",crashReport);

        onCrash();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        return true;
    }

    protected abstract void onCrash();

    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
//        String mtype = android.os.Build.MODEL; // 手机型号
//        String mtyb= android.os.Build.BRAND;//手机品牌
//        String cpuAbi = android.os.Build.CPU_ABI;//cpu架构
//        String sdk = android.os.Build.VERSION.SDK;//sdk版本号
//        String release = android.os.Build.VERSION.RELEASE;//依赖的系统版本号
//        String device = android.os.Build.DEVICE;//设备
//        String manufacturer = android.os.Build.MANUFACTURER;//手机品牌
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.MODEL + ")\n");

        Date date = new Date();
        DateFormat format = DateFormat
                .getDateTimeInstance();
        String t = format.format(date);
        exceptionStr.append("time: " + t + "\n");

        exceptionStr.append("Exception: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }

}