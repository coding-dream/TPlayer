package com.less.tplayer.va;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Environment;

import java.io.File;

/**
 * Created by deeper on 2017/11/17.
 */

public class AppTarget {
    private static AppTarget sAppTarget;

    public static AppTarget get(Context context) {
        if (sAppTarget == null) {
            synchronized (AppTarget.class) {
                if (sAppTarget == null) {
                    sAppTarget = new AppTarget(context);
                }
            }
        }
        return sAppTarget;
    }

    private static final String PKG = "com.dbo.musicspy";
    private Context mContext;
    private final SharedPreferences mSharedPreferences;
    private String mApkFile,mUpdateFile;
    private boolean fromSystem, firstInstall;

    private AppTarget(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("va_settings", Context.MODE_MULTI_PROCESS);
        // 从系统已安装的apk中安装插件,默认为false
        fromSystem = mSharedPreferences.getBoolean("fromSystem", false);
        firstInstall = mSharedPreferences.getBoolean("install", false);
        mApkFile = new File(Environment.getExternalStorageDirectory(), "debug.apk").getAbsolutePath();
        mUpdateFile = mApkFile;
        if (!firstInstall) {
            PackageInfo packageInfo = PackageUtils.getPackageInfo(context, PKG);
            // 如果从系统中查找该包名存在,说明系统中已安装过此apk, 则可以从系统中加载此插件.
            if (packageInfo != null) {
                fromSystem = true;
                mApkFile = packageInfo.applicationInfo.publicSourceDir;
            }
        }
    }

    public void update(){
        PackageInfo packageInfo = PackageUtils.getPackageInfo(mContext, PKG);
        if (packageInfo != null) {
            fromSystem = true;
            mApkFile = packageInfo.applicationInfo.publicSourceDir;
        }
        mSharedPreferences.edit()
                .putBoolean("install", false)
                .putBoolean("fromSystem", fromSystem)
                .apply();
    }

    public void onUninstall() {
        update();
    }

    public void onInstall() {
        mSharedPreferences.edit()
                .putBoolean("install", true)
                .putBoolean("fromSystem", fromSystem)
                .apply();
    }

    /**
     * 安装包路径
     */
    public String getInstallPackagePath() {
        return mApkFile;
    }

    public String getUpdateFile() {
        return mUpdateFile;
    }

    /**
     * 从apk安装，则false
     */
    public boolean isInstallBySystemApp() {
        return fromSystem;
    }

    /**
     * 目标包名
     */
    public String getPackageName() {
        return PKG;
    }
}
