package com.less.tplayer;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.less.tplayer.va.VApplication;
import com.less.tplayer.util.ReadState;
import com.less.tplayer.util.ReadStateHelper;

import java.util.List;

/**
 * @author Administrator
 */
public class TpApplication extends VApplication {
    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static final String CONFIG_READ_STATE_PRE = "CONFIG_READ_STATE_PRE_";
    private static TpApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (isCurrentProcess()) {
            instance = this;
            TpCrashHandler.init();
        }
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return AppContext
     */
    public static TpApplication getContext() {
        return instance;
    }

    /**
     * 获取已读状态管理器
     *
     * @param mark 传入标示，如：博客：blog; 新闻：news
     * @return 已读状态管理器
     */
    public static ReadState getReadState(String mark) {
        ReadStateHelper helper = ReadStateHelper.create(getContext(),
                CONFIG_READ_STATE_PRE + mark, 100);
        return new ReadState(helper);
    }

    private boolean isCurrentProcess() {
        String processName = getProcessName(this, Process.myPid());
        if (processName == null || !processName.equals(BuildConfig.APPLICATION_ID)) {
            return false;
        }
        return true;
    }

    private String getProcessName(Context cxt, int pid) {
        List<ActivityManager.RunningAppProcessInfo> runningApps = ((ActivityManager) cxt.getSystemService(ACTIVITY_SERVICE)).getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
