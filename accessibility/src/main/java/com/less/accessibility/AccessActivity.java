package com.less.accessibility;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        findViewById(R.id.btn_check).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_authorized) {

        } else if (id == R.id.btn_wifi) {

        } else if (id == R.id.btn_check) {
            boolean enabled = AccessibilityUtil.checkAccessibilityEnabled(this,QQAccessibilityService.class);
            Toast.makeText(this, "enabled:" + enabled, Toast.LENGTH_SHORT).show();
        }
    }
}
