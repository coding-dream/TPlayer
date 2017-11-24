package com.less.tplayer.mvp.feature;

import android.support.v7.app.ActionBar;

import com.less.tplayer.R;
import com.less.tplayer.base.activity.BaseActivity;

/**
 * @author Administrator
 */
public class FeatureActivity extends BaseActivity {

    @Override
    protected void initToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        FeatureFragment fragment = FeatureFragment.newInstance();
        addFragment(R.id.fl_content, fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feature;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
