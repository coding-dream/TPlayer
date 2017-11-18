package com.less.tplayer.va;

import android.app.Application;
import android.content.Context;

import com.less.tplayer.BuildConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.VASettings;

/**
 * Created by deeper on 2017/11/17.
 */

public class VApplication extends Application {
    public static final String ACTION_PACKAGE_ADD = BuildConfig.APPLICATION_ID + ".action.pacakage.add";
    public static final String ACTION_PACKAGE_UPDATE = BuildConfig.APPLICATION_ID + ".action.pacakage.update";
    public static final String ACTION_PACKAGE_REMOVE = BuildConfig.APPLICATION_ID + ".action.pacakage.remove";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // io重定向
        VASettings.ENABLE_IO_REDIRECT = true;
        // 允许app发送快捷方式
        VASettings.ENABLE_INNER_SHORTCUT = false;
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final VirtualCore virtualCore = VirtualCore.get();
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {

            @Override
            public void onMainProcess() {
                // TODO 主进程初始化
            }

            @Override
            public void onVirtualProcess() {
                // activity生命周期监听
                virtualCore.setComponentDelegate(new MyComponentDelegate());
                // 信息伪造
                // 任务历史显示,activity启动的intent处理（不显示任务）
                virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
            }

            @Override
            public void onServerProcess() {
                // 监听全部插件的安装和卸载
                virtualCore.registerObserver(new MyPackageObserver(VApplication.this));
                // 通过intent 安装/卸载 的监听器
                virtualCore.setAppRequestListener(new MyAppRequestListener(VApplication.this));
                //允许内部app调用外部的app名单
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqq");
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqqi");
                virtualCore.addVisibleOutsidePackage("com.tencent.minihd.qq");
                virtualCore.addVisibleOutsidePackage("com.tencent.qqlite");
                virtualCore.addVisibleOutsidePackage("com.facebook.katana");
                virtualCore.addVisibleOutsidePackage("com.whatsapp");
                virtualCore.addVisibleOutsidePackage("com.tencent.mm");
                virtualCore.addVisibleOutsidePackage("com.immomo.momo");
            }
        });
    }
}
