package com.less.tplayer.fragment;

import android.os.Bundle;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseTitleFragment;

/**
 * Created by deeper on 2017/11/22.
 */

public class TweetViewPagerFragment extends BaseTitleFragment {

    @Override
    protected int getToolBarIconRes() {
        return R.mipmap.actionbar_search_icon;
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
