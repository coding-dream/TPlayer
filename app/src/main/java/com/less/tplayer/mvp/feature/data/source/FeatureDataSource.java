package com.less.tplayer.mvp.feature.data.source;

import com.less.tplayer.mvp.feature.data.Feature;

import java.util.List;

/**
 * Created by deeper on 2017/11/24.
 */

public interface FeatureDataSource {

    interface LoadCallback {

        void onDataLoaded(List<Feature> datas);

        void onDataNotAvailable();
    }

    void getDatasByPage(int page,LoadCallback callback);
}
