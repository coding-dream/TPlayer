package com.less.test;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * adb shell dumpsys activity | grep "mFocusedActivity"
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int times = 0;

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_install).setOnClickListener(this);
        findViewById(R.id.btn_accesibility).setOnClickListener(this);
        findViewById(R.id.btn_alreadyInstall).setOnClickListener(this);
        findViewById(R.id.btn_developer).setOnClickListener(this);
        findViewById(R.id.btn_wifi).setOnClickListener(this);
        findViewById(R.id.btn_usb_debug).setOnClickListener(this);
        findViewById(R.id.btn_aspider).setOnClickListener(this);
        findViewById(R.id.btn_aspider_setting).setOnClickListener(this);
        findViewById(R.id.btn_pass1).setOnClickListener(this);
        findViewById(R.id.btn_pass2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_install) {
            String packageName = "com.android.settings";
            String className = "com.android.settings.Settings$VivoApplicationSettingsActivity";
            ComponentName componentName = new ComponentName(packageName, className);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.setAction("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.btn_accesibility) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } else if (id == R.id.btn_alreadyInstall) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        } else if (id == R.id.btn_developer) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            startActivity(intent);
        } else if (id == R.id.btn_wifi) {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        } else if (id == R.id.btn_usb_debug) {
            String packageName = "com.android.settings";
            String className = "com.android.settings.DevelopmentSettings";
            ComponentName componentName = new ComponentName(packageName, className);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.setAction("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.btn_aspider) {
            if (times > 7) {
                Intent intent = new Intent();
                intent.setClass(this, SpiderActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.btn_aspider_setting) {
            if (times > 7) {
                Intent intent = new Intent();
                intent.setClass(this, SpiderSettingActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.btn_pass1) {
            if (times % 2 == 0) {
                times ++;
            }
        } else if (id == R.id.btn_pass2) {
            if (times % 2 != 0) {
                times ++;
            }
        }
    }
}
