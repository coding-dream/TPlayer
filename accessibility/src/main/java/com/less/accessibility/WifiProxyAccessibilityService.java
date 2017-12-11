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
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
                if (accessibilityNodeInfo != null) {

                    // 模拟点击WLAN
                    List<AccessibilityNodeInfo> wlan = accessibilityNodeInfo.findAccessibilityNodeInfosByText("WLAN");
                    if (wlan != null && wlan.size() > 0) {
                        wlan.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    // 模拟点击TP-LINK_F770
                    List<AccessibilityNodeInfo> wlan_f770 = accessibilityNodeInfo.findAccessibilityNodeInfosByText("TP-LINK_F770");
                    if (wlan_f770 != null && wlan_f770.size() > 0) {
                        List<AccessibilityNodeInfo> wlan_f770_linear_icon = wlan_f770.get(0).getParent().findAccessibilityNodeInfosByViewId("com.android.wifisettings:id/advance_layout");
                        if (wlan_f770_linear_icon != null && wlan_f770_linear_icon.size() > 0) {
                            wlan_f770_linear_icon.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                    // 模拟点击 手动代理
                    List<AccessibilityNodeInfo> proxy = accessibilityNodeInfo.findAccessibilityNodeInfosByText("手动代理");
                    if (proxy != null && proxy.size() > 0) {
                        AccessibilityNodeInfo layout = proxy.get(0).getParent();
                        List<AccessibilityNodeInfo> checkBox = layout.findAccessibilityNodeInfosByViewId("android:id/checkbox");
                        if (checkBox != null && checkBox.size() > 0) {

                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Toast.makeText(this, "Notify", Toast.LENGTH_SHORT).show();
                event.getSource().recycle();
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;
            default:
                break;
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
