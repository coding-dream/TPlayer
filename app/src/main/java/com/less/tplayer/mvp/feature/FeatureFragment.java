package com.less.tplayer.mvp.feature;

import android.os.Bundle;
import android.view.View;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;

/**
 * Created by deeper on 2017/11/24.
 */

public class FeatureFragment extends BaseFragment implements FeatureContract.View{

    public static FeatureFragment newInstance() {
        return new FeatureFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View mRoot) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feature;
    }

    @Override
    protected void initBundle(Bundle bundle) {

    }

    @Override
    public void setPresenter(FeatureContract.Presenter presenter) {

    }
}
