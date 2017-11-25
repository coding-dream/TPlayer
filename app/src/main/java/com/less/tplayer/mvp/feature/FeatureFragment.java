package com.less.tplayer.mvp.feature;

import android.os.Bundle;
import android.view.View;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;
import com.less.tplayer.mvp.feature.data.Feature;

import java.util.List;

/**
 * Created by deeper on 2017/11/24.
 */

public class FeatureFragment extends BaseFragment implements FeatureContract.View {
    private FeatureContract.Presenter mPresenter;

    public static FeatureFragment newInstance() {
        return new FeatureFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();;
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
        mPresenter = presenter;
    }

    @Override
    public void showRefreshSuccess(List<Feature> datas) {

    }

    @Override
    public void showLoadMoreSuccess(List<Feature> datas) {

    }

    @Override
    public void showNoMore() {

    }

    @Override
    public void showComplete() {

    }

    @Override
    public void showNetworkError(int strId) {

    }
}
