package com.less.tplayer.fragment;

import android.os.Bundle;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseTitleFragment;
import com.less.tplayer.interfaces.IFragmentReSelected;

/**
 * Created by deeper on 2017/11/23.
 */

public class MovieFragment extends BaseTitleFragment implements IFragmentReSelected {

    public static final String CATALOG_KEY = "key";
    public static final int CATALOG_NEW = 1;
    public static final int CATALOG_HOT = 2;
    public static final int CATALOG_MYSELF = 3;

    @Override
    protected int getToolBarIconRes() {
        return 0;
    }

    @Override
    protected int getToolBarTitleRes() {
        return 0;
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

    @Override
    public void callReSelect() {

    }
}
