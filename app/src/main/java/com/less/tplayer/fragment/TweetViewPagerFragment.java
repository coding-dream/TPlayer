package com.less.tplayer.fragment;

import android.os.Bundle;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseViewPagerFragment;

/**
 * Created by deeper on 2017/11/22.
 */

public class TweetViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected int getToolBarIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected int getToolBarTitleRes() {
        return R.string.main_tab_name_tweet;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_tweet;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initBundle(Bundle bundle) {

    }
}
