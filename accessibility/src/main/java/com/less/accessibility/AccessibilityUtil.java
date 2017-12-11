package com.less.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.File;
import java.util.List;

/**
 * Created by deeper on 2017/12/11.
 */

public class AccessibilityUtil {

    private static final String TYPE_CLASS_BUTTON = "android.widget.Button";
    private static final String TYPE_CLASS_TEXT_VIEW = "android.widget.TextView";
    private static final String TYPE_CLASS_IMAGE_VIEW = "android.widget.ImageView";
    private static final String TYPE_CLASS_LIST_VIEW = "android.widget.AbsListView";

    /**
     * 检测当前辅助服务是否开启
     */
    public static boolean checkAccessibilityEnabled(Context context,Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
            // ignored
        }
        TextUtils.SimpleStringSplitter stringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                stringColonSplitter.setString(settingValue);
                while (stringColonSplitter.hasNext()) {
                    String accessibilityService = stringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void openAccessibilityUI(Context context){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开系统设置界面
     * @param context
     */
    public static void openSettingUI(Context context){
        // com.android.wifisettings/.wifi.WifiApOptionSettings
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开包名packageName的清理界面
     * @param context
     * @param packageName
     */
    public static void openClearAppUI(Context context,String packageName){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 安装apk文件
     * @param context
     * @param apkPath
     */
    public static void openInstallUI(Context context,String apkPath){
        Uri uri = Uri.fromFile(new File(apkPath));
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(localIntent);
    }
    /**
     * 打开包名packageName的App启动界面
     * @param context
     * @param packageName
     */
    public void openApp(Context context,String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static boolean isButton(AccessibilityNodeInfo node){
        if (TYPE_CLASS_BUTTON.equals(node.getClassName())) {
            return true;
        }
        return false;
    }

    /**
     * 通过id查找节点
     * <p>
     *     参数resId格式: com.android.wifisettings:id/advance_layout
     * </p>
     * @param nodeInfo
     * @param resId
     * @return
     */
    public static AccessibilityNodeInfo findNodeById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if(list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 通过文本查找节点(注: text是包含contains,而不是完全equals)
     * @param nodeInfo
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findNodeByText(AccessibilityNodeInfo nodeInfo, String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 模拟点击(如果该节点不可点击,事件分发原理,尝试递归其所有parent节点)
     * @param nodeInfo
     */
    public static void performClick(AccessibilityNodeInfo nodeInfo){
        if(nodeInfo == null) {
            return;
        }
        if(nodeInfo.isClickable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    /**
     * 模拟输入
     */
    public static void performInputText(Context context,AccessibilityNodeInfo node, String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    /**
     * 根据收到的[通知事件] 打开通知
     * @param event
     */
    public void openAppByNotification(AccessibilityEvent event) {
        if (event.getParcelableData() != null  && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            // 获取通知的内容 ...
            String content = notification.tickerText.toString();
            try {
                PendingIntent pendingIntent = notification.contentIntent;
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 模拟上滑操作方式一(列表数据则向下移动,似乎是查看新数据一样,如ListView)
     */
    public void performScrollForward1(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
    }

    /**
     * 模拟上滑操作方式二(Node参数方式,针对于ListView,不推荐)
     */
    public void performScrollForward2(AccessibilityNodeInfo node) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
    }

    /**
     * 模拟下滑操作方式一(列表数据则向上移动,似乎是查看旧数据一样,如ListView)
     */
    public void performScrollBackward1(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        }
    }

    /**
     * 模拟下滑操作方式二(Node参数方式,针对于ListView,不推荐)
     */
    public void performScrollBackward2(AccessibilityNodeInfo node) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        }
    }

    /**
     * 返回HOME键
     * @param service
     */
    public static void performGlobalHome(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        }
    }

    /**
     * 返回Back键
     * @param service
     */
    public static void performGlobalBack(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    /**
     * 最近打开应用列表
     * @param service
     */
    public static void performGlobaRecentsApp(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
        }
    }

    /**
     * 打开通知栏
     * @param service
     */
    public static void performGlobaNotification(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
        }
    }

    /**
     * 锁屏
     * @param service
     */
    public static void performGlobalPower(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
        }
    }

    /**
     * 设置
     * @param service
     */
    public static void performGlobalQuickSetting(AccessibilityService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS);
        }
    }

    public static void findNodeAllParents(AccessibilityNodeInfo topNode,List<AccessibilityNodeInfo> list){
        if (topNode.getParent() != null) {
            AccessibilityNodeInfo parent = topNode.getParent();
            list.add(parent);
            findNodeAllParents(parent,list);
        }
    }

    public static void findNodeAllChildren(AccessibilityNodeInfo topNode,List<AccessibilityNodeInfo> list){
        if (topNode.getChildCount() > 0) {
            for(int i = 0;i < topNode.getChildCount();i++) {
                AccessibilityNodeInfo child = topNode.getChild(i);
                list.add(child);
                findNodeAllChildren(child,list);
            }
        }
    }

    public static boolean hasChild(AccessibilityNodeInfo node){
        if (node.getChildCount() == 0) {
            return false;
        }
        return true;
    }

    /**
     * clazz1 是 clazz2 的父类
     * 如(android.widget.AbsListView).isAssignableFrom(android.widget.ListView)
     * @return
     */
    public static boolean oneIsAnotherParent(String _clazz1,String _clazz2){
        try {
            Class<?> clazz1 = Class.forName(_clazz1);
            Class<?> clazz2 = Class.forName(_clazz2);

            if (clazz1.isAssignableFrom(clazz2)) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
