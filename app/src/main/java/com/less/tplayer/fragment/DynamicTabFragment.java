package com.less.tplayer.fragment;

import android.os.Bundle;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseTitleFragment;

/**
 *
 * @author deeper
 * @date 2017/11/22
 */

public class DynamicTabFragment extends BaseTitleFragment{

    @Override
    protected int getToolBarIconRes() {
        return R.mipmap.actionbar_search_icon;
    }

    @Override
    protected int getToolBarTitleRes() {
        return R.string.main_tab_name_news;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_dynamic_tab;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initBundle(Bundle bundle) {

    }
}
