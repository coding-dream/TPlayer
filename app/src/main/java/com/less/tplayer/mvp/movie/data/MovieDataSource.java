package com.less.tplayer.mvp.movie.data;

import java.util.List;

/**
 * Created by deeper on 2017/11/24.
 */

public interface MovieDataSource {

    interface LoadCallback {

        void onDataLoaded(List<Movie> datas);

        void onDataNotAvailable();
    }

    void getDatasByPage(String type,int page, LoadCallback callback);
}
