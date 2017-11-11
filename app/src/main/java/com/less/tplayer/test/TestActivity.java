package com.less.tplayer.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.less.tplayer.R;

/**
 * @author Administrator
 */
public class TestActivity extends AppCompatActivity {
    private static final String TAG = "less";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    public void handle(View view) {

//        Intent intent = new Intent();
//        intent.setClass(this, com.less.bzrefreshbtn.TestActivity.class);
//        startActivity(intent);
    }
}
