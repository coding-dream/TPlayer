package com.less.tplayer.mvp.feature.data.source;

import com.less.tplayer.mvp.feature.data.source.local.FeatureLocalDataSource;
import com.less.tplayer.mvp.feature.data.source.remote.FeatureRemoteDataSource;

/**
 * Created by deeper on 2017/11/25.
 */

public class FeatureRepository implements FeatureDataSource {

    private static FeatureRepository INSTANCE = null;

    private final FeatureDataSource mRetemoDataSource;

    private final FeatureDataSource mLocalDataSource;

    public FeatureRepository() {
        this.mLocalDataSource = new FeatureLocalDataSource();
        this.mRetemoDataSource = new FeatureRemoteDataSource();
    }

    @Override
    public void getDatasByPage(int page, LoadCallback callback) {

    }
}
