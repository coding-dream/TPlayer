package com.less.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * @author Administrator
 */
public class WifiProxyAccessibilityService extends AccessibilityService {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
            if (accessibilityNodeInfo != null) {
                List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByText("WLAN");
                if (list != null && list.size() > 0) {
                    for (AccessibilityNodeInfo n : list) {
                        n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }
    }

    /**
     * 系统想要中断AccessibilityService返给的响应时会调用
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "onInterrupt", Toast.LENGTH_SHORT).show();
    }
}
