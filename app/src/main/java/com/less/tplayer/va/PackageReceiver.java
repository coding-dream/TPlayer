package com.less.tplayer.va;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserHandle;

/**
 * Created by deeper on 2017/11/17.
 * 如果插件是从系统应用安装，监听广播，自动更新和卸载
 */

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("server", "PackageReceiver " + action);
        if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (VirtualCore.get().isAppInstalled(packageName)) {
                Log.d("server", "update " + packageName);
                VirtualCore.get().killApp(packageName, VUserHandle.USER_ALL);
                PackageInfo packageInfo = PackageUtils.getPackageInfo(context, packageName);
                if (packageInfo != null) {
                    Log.i("server", "installPackage " + packageName);
                    VirtualCore.get().installPackage(packageInfo.applicationInfo.publicSourceDir,
                            InstallStrategy.UPDATE_IF_EXIST);
                }
            }
        }else if(Intent.ACTION_PACKAGE_REMOVED.equals(action)){
            String packageName = intent.getData().getSchemeSpecificPart();
            if(!PackageUtils.isInstall(context, packageName)){
                VirtualCore.get().uninstallPackage(packageName);
            }
        }
    }
}
