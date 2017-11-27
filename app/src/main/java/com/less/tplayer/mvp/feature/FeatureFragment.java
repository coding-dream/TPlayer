package com.less.tplayer.mvp.feature;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;
import com.less.tplayer.mvp.feature.data.Feature;
import com.less.tplayer.widget.RecyclerRefreshLayout;

import java.util.List;

/**
 * Created by deeper on 2017/11/24.
 */

public class FeatureFragment extends BaseFragment implements FeatureContract.View {
    private FeatureContract.Presenter mPresenter;

    protected RecyclerRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected FeatureAdapter mAdapter;

    public static FeatureFragment newInstance() {
        return new FeatureFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mPresenter.start();;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View mRoot) {
        mRefreshLayout = (RecyclerRefreshLayout) mRoot.findViewById(R.id.refreshLayout);
        mRefreshLayout.setSuperRefreshLayoutListener(new RecyclerRefreshLayout.SuperRefreshLayoutListener() {
            @Override
            public void onRefreshing() {
                mAdapter.setState(FeatureAdapter.STATE_HIDE, true);
                mPresenter.doRefresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.doLoadMore();
                mAdapter.setState(FeatureAdapter.STATE_LOADING, true);
            }
        });
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.recyclerView);
        mAdapter = new FeatureAdapter(getContext(),FeatureAdapter.ONLY_FOOTER);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FeatureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                // handle click
            }
        });
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
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
        mAdapter.resetItem(datas);
    }

    @Override
    public void showLoadMoreSuccess(List<Feature> datas) {
        mAdapter.addAll(datas);
    }

    @Override
    public void showNoMore() {
        mAdapter.setState(FeatureAdapter.STATE_NO_MORE, true);
    }

    @Override
    public void showComplete() {
        mRefreshLayout.onComplete();
    }

    @Override
    public void showNetworkError(int strId) {
        mAdapter.setState(FeatureAdapter.STATE_INVALID_NETWORK, true);
    }
}
