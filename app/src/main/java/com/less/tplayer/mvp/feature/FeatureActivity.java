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

        // todo 这个地方是模仿Google官方code,但是业务又稍有不同,Google可能改变此处代码为依赖注入方式,由于某原因暂无阅读某模块源码,待定为低耦合方式.

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
