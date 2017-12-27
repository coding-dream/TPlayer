package com.less.tplayer.mvp.movie.data;

import com.less.tplayer.mvp.feature.data.Feature;

import java.util.List;

/**
 * Created by deeper on 2017/11/24.
 */

public interface MovieDataSource {

    interface LoadCallback {

        void onDataLoaded(List<Feature> datas);

        void onDataNotAvailable();
    }

    void getDatasByPage(int page, LoadCallback callback);
}
