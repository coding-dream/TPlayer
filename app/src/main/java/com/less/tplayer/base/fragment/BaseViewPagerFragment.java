package com.less.tplayer.base.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.less.tplayer.R;

/**
 * Created by deeper on 2017/11/23.
 */

public abstract class BaseViewPagerFragment extends BaseTitleFragment {
    protected ViewPager mBaseViewPager;
    protected TabLayout mTabNav;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_base_viewpager;
    }

    @Override
    protected void initView(View mRoot) {
        super.initView(mRoot);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initBundle(Bundle bundle) {

    }
}
