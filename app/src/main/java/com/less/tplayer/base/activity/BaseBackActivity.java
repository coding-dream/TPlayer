package com.less.tplayer.base.activity;

import android.support.v7.app.ActionBar;

/**
 *
 * @author deeper
 * @date 2017/11/12
 */

public abstract class BaseBackActivity extends BaseActivity {

    @Override
    protected void initToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
