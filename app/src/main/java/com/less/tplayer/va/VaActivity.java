package com.less.tplayer.va;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.less.tplayer.R;
import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
public class VaActivity extends AppCompatActivity {
    private static final String TAG = VaActivity.class.getSimpleName();
    private AppTarget appTarget = null;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (VApplication.ACTION_PACKAGE_ADD.equals(intent.getAction())
                    || VApplication.ACTION_PACKAGE_UPDATE.equals(intent.getAction())) {
                Toast.makeText(context, "应用安装/更新", Toast.LENGTH_SHORT).show();
                String packageName = intent.getData().getSchemeSpecificPart();
            } else if (VApplication.ACTION_PACKAGE_REMOVE.equals(intent.getAction())) {
                Toast.makeText(context, "应用卸载", Toast.LENGTH_SHORT).show();
                String packageName = intent.getData().getSchemeSpecificPart();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        appTarget = AppTarget.get(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VApplication.ACTION_PACKAGE_ADD);
        intentFilter.addAction(VApplication.ACTION_PACKAGE_UPDATE);
        intentFilter.addAction(VApplication.ACTION_PACKAGE_REMOVE);
        intentFilter.addDataScheme("package");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public void handle(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                int userId = 0;
                // 启动sdcard的apk文件需要先preOpt, 启动手机已安装的apk则不需要preOpt.
                if (!appTarget.isInstallBySystemApp()) {
                    try {
                        VirtualCore.get().preOpt(appTarget.getPackageName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = VirtualCore.get().getLaunchIntent(appTarget.getPackageName(), userId);
                if (intent == null) {
                    Toast.makeText(this, "启动失败: intent == null !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "启动应用", Toast.LENGTH_SHORT).show();
                    VActivityManager.get().startActivity(intent, userId);
                }
                break;
            case R.id.btn_install:
                File file = new File(appTarget.getInstallPackagePath());
                if (file.exists()) {
                    //对比版本InstallStrategy.COMPARE_VERSION
                    int flags = InstallStrategy.COMPARE_VERSION;
                    if (appTarget.isInstallBySystemApp()) {
                        flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
                    }
                    InstallResult installResult = VirtualCore.get().installPackage(file.getAbsolutePath(), flags);
                    if (installResult != null && installResult.isSuccess) {
                        appTarget.onInstall();
                        Toast.makeText(this, "安装成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "安装失败: " + installResult.error, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, installResult.error);
                    }
                }
                break;
            case R.id.btn_delete:
                if (VirtualCore.get().uninstallPackage(appTarget.getPackageName())) {
                    Toast.makeText(this, "卸载成功", Toast.LENGTH_SHORT).show();
                    AppTarget.get(this).onUninstall();
                } else {
                    Toast.makeText(this, "卸载失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update:
                File updateFile = new File(AppTarget.get(this).getUpdateFile());
                if (updateFile.exists()) {
                    //停止运行
                    VirtualCore.get().killApp(AppTarget.get(this).getPackageName(), VUserHandle.USER_ALL);
                    InstallResult rs = VirtualCore.get().installPackage(updateFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
                    if (rs == null || !rs.isSuccess) {
                        Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void getThePackageInfo(){
        if (VirtualCore.get().isAppInstalled(appTarget.getPackageName())) {
            InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(AppTarget.get(this).getPackageName(), 0);
            if (installedAppInfo == null) {
                Toast.makeText(this, "获取安装信息失败", Toast.LENGTH_SHORT).show();
            } else {
                PackageInfo packageInfo = installedAppInfo.getPackageInfo(0);
                if (packageInfo == null) {
                    Toast.makeText(this, "获取包信息失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "包信息: " + packageInfo.versionName + " " + packageInfo.versionCode, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}