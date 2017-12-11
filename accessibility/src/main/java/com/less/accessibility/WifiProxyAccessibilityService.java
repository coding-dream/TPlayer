package com.less.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author Administrator
 */
public class WifiProxyAccessibilityService extends AccessibilityService {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        CharSequence packageName = event.getPackageName();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                if (rootNode != null) {

                    if (packageName.equals("com.android.settings")) {
                        AccessibilityNodeInfo settingNode = AceHelper.findNodeByText(rootNode,"WLAN");
                        AceHelper.performClick(settingNode);
                        rootNode.recycle();
                    } else if (packageName.equals("com.android.wifisettings")) {
                        AccessibilityNodeInfo wifiNode = AceHelper.findNodeByText(rootNode, "TP-LINK_F770");
                        AccessibilityNodeInfo wifiNodeParent = wifiNode.getParent();
                        AccessibilityNodeInfo iconNode = AceHelper.findNodeById(wifiNodeParent, "com.android.wifisettings:id/advance_layout");
                        AceHelper.performClick(iconNode);
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
