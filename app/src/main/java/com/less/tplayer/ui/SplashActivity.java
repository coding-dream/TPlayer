package com.less.tplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;
import com.less.tplayer.R;
import com.less.tplayer.util.SharedPreferenceUtils;

/**
 * @author Administrator
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        // StatusBarUtil.setColor(SplashActivity.this, getResources().getColor(R.color.colorPrimary));
        // StatusBarUtil.setTranslucent 半透明
        // StatusBarUtil.setTransparent(this);全透明
         StatusBarUtil.setTranslucent(this,50);
        // 这步取值操作不能放在run中，否则会执行两次导致页面跳转出错
        final boolean isFirst = SharedPreferenceUtils.getBooleanData("isFirst", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (isFinishing()) {
                    return;
                }
                if (isFirst) {
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                    SharedPreferenceUtils.setBooleanData("isFirst", false);

                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },3000);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}