package com.less.tplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.less.tplayer.TpApplication;

/**
 * Author： yolanda
 * <p>
 * CreateTime： 2016/12/6 0006 下午 1:25
 * <p>
 * description：SP工具类
 */

public class SharedPreferenceUtils {
    private static final String FILENAME = "config";
    private static SharedPreferences.Editor editor;

    public static Context getContext() {
        return TpApplication.getContext();
    }

    public static SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:25
     * <p>
     * description：获取Boolean类型数据
     */

    public static Boolean getBooleanData(String key, Boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:32
     * <p>
     * description：保存Boolean数据
     */

    public static void setBooleanData(String key, Boolean value) {
        editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:33
     * <p>
     * description：获取String类型数据
     */

    public static String getStringData(String key, String value) {
        return getSharedPreferences().getString(key, value);
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:35
     * <p>
     * description：保存String类型数据
     */

    public static void setStringData(String key, String value) {
        editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:36
     * <p>
     * description：获取int类型数据
     */

    public static int getIntData(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:39
     * <p>
     * description：保存int类型数据
     */

    public static void setIntData(String key, int value) {
        editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:40
     * <p>
     * description：获取long类型数据
     */

    public static long getLongData(String key, long value) {
        return getSharedPreferences().getLong(key, value);
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:42
     * <p>
     * description：保存long类型数据
     */

    public static void setLongData(String key, long value) {
        editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:43
     * <p>
     * description：保存float类型数据
     */

    public static void setFloatData(String key, float value) {
        editor = getSharedPreferences().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Author： yolanda
     * <p>
     * CreateTime： 2016/12/6 0006 下午 1:45
     * <p>
     * description：获取float类型数据
     */

    public static float getFloatData(String key, float value) {
        return getSharedPreferences().getFloat(key, value);
    }
}


