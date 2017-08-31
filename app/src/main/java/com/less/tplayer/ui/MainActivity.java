package com.less.tplayer.ui;

import android.os.Bundle;

import com.less.tplayer.R;
import com.less.tplayer.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public int getContentView() {
        return 0;
    }
}
