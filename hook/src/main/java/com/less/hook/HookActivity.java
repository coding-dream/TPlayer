package com.less.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class HookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook);


    }

    public void handle(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("https://www.baidu.com"));

        // 注意这里使用的ApplicationContext 启动的Activity,即hook ContextImpl
        // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
        // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
        // 比较简单, 直接替换这个Activity的此字段即可.
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        // 在这里进行Hook
        try {
            HookHelper.attachContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
