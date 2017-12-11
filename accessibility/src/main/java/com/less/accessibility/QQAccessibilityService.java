package com.less.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class QQAccessibilityService extends AccessibilityService {
    private AccessibilityNodeInfo currentNode = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                // 注意由于,ListView的ViewHolder作用,如果模拟滚动后,虽然状态改变,但是再次触发查找[搜索]肯定找不到了,所以需要我们保存之前查找过的信息.
                List<AccessibilityNodeInfo> qqGroup = rootNode.findAccessibilityNodeInfosByText("搜索");
                if (qqGroup != null && qqGroup.size() > 0) {
                    List<AccessibilityNodeInfo> list = new ArrayList<>();
                    AccessibilityUtil.findNodeAllParents(qqGroup.get(0),list);
                    for (AccessibilityNodeInfo node : list) {
                        node.getClassName();

                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "onInterrupt", Toast.LENGTH_SHORT).show();
    }
}
