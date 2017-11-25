package com.less.tplayer.mvp.feature;

import com.less.tplayer.mvp.feature.data.source.FeatureRepository;

/**
 * Created by deeper on 2017/11/25.
 */

public class FeaturePresenter implements FeatureContract.Presenter {
    private FeatureRepository mFeatureRepository;
    private FeatureContract.View view;

    public FeaturePresenter(FeatureRepository featureRepository, FeatureFragment fragment) {
        mFeatureRepository = featureRepository;
        view = fragment;
    }

    @Override
    public void start() {

    }
}
