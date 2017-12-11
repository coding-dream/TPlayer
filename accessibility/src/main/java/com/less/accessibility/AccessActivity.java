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
        findViewById(R.id.btn_clear).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_authorized) {
            AceHelper.openAccessibilityUI(this);
        } else if (id == R.id.btn_wifi) {
            AceHelper.openSettingUI(this);
        } else if (id == R.id.btn_check) {
            boolean enabled = AceHelper.checkAccessibilityEnabled(this,WifiProxyAccessibilityService.class);
            Toast.makeText(this, "enabled:" + enabled, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_clear) {
            AceHelper.openClearAppUI(this,"com.qiyi.video");
        }
    }
}
