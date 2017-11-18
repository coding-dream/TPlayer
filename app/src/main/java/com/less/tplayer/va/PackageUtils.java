package com.less.tplayer.va;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by deeper on 2017/11/17.
 */

public class PackageUtils {
    public static PackageInfo getPackageInfo(Context context, String pkg) {
        try {
            return context.getPackageManager().getPackageInfo(pkg, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInstall(Context context, String pkg) {
        return getPackageInfo(context, pkg) != null;
    }
}
