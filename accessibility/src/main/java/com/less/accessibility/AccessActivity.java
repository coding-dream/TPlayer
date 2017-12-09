package com.less.accessibility;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

/**
 *
 * @author deeper
 * @date 2017/12/9
 * 描述: 获取当前栈顶Activity adb shell dumpsys activity | grep "mFocusedActivity"
 *
 */
public class AccessActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        findViewById(R.id.btn_authorized).setOnClickListener(this);
        findViewById(R.id.btn_wifi).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_authorized) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.btn_wifi) {
            // com.android.wifisettings/.wifi.WifiApOptionSettings
             ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings");
             Intent intent = new Intent();
             intent.setComponent(componentName);
             intent.setAction("android.intent.action.VIEW");
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
        }
    }
}
