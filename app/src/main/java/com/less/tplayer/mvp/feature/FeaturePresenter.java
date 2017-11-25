package com.less.tplayer.mvp.feature;

import com.less.tplayer.R;
import com.less.tplayer.mvp.feature.data.Feature;
import com.less.tplayer.mvp.feature.data.source.FeatureDataSource;
import com.less.tplayer.mvp.feature.data.source.FeatureRepository;

import java.util.List;

/**
 * Created by deeper on 2017/11/25.
 */

public class FeaturePresenter implements FeatureContract.Presenter {
    private static final int PAGE_SIZE = 20;
    private FeatureRepository mFeatureRepository;
    private FeatureContract.View mView;
    private int mPage = 1;

    public FeaturePresenter(FeatureRepository featureRepository, FeatureFragment fragment) {
        mFeatureRepository = featureRepository;
        mView = fragment;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        doRefresh();
    }

    @Override
    public void doRefresh() {
        mFeatureRepository.getDatasByPage(1, new FeatureDataSource.LoadCallback() {
            @Override
            public void onDataLoaded(List<Feature> datas) {
                if(datas != null && datas.size() > 0){
                    mView.showRefreshSuccess(datas);
                    if (datas.size() < PAGE_SIZE) {
                        mView.showNoMore();
                    }
                    mPage = 2;
                }
                mView.showComplete();
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNetworkError(R.string.state_network_error);
                mView.showComplete();
            }
        });
    }

    @Override
    public void doLoadMore() {
        mFeatureRepository.getDatasByPage(mPage, new FeatureDataSource.LoadCallback() {
            @Override
            public void onDataLoaded(List<Feature> datas) {
                if(datas != null && datas.size() > 0){
                    mView.showLoadMoreSuccess(datas);
                    if (datas.size() < PAGE_SIZE) {
                        mView.showNoMore();
                    }
                    mPage = mPage + 1;
                }
                mView.showComplete();
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNetworkError(R.string.state_network_error);
                mView.showComplete();
            }
        });
    }
}
