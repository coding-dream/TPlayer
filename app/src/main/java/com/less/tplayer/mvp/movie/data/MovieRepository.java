package com.less.tplayer.mvp.movie.data;

import android.os.Handler;
import android.os.Looper;

import com.less.tplayer.mvp.feature.data.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2017/11/25.
 */

public class MovieRepository implements MovieDataSource {

    private static MovieRepository INSTANCE = null;

    private final MovieDataSource mRetemoDataSource;

    private final MovieDataSource mLocalDataSource;

    private Handler handler = new Handler(Looper.getMainLooper());

    public MovieRepository() {
        this.mLocalDataSource = new MovieLocalDataSource();
        this.mRetemoDataSource = new MovieRemoteDataSource();
    }

    @Override
    public void getDatasByPage(final int page, final LoadCallback callback) {
        // 模拟数据
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final List<Feature> datas = new ArrayList<>();
                    int PAGE_SIZE = 20;
                    if (page == 4) {
                        PAGE_SIZE = 10;
                    }

                    if(page > 4){
                        PAGE_SIZE = 0;
                    }
                    for(int i = 0;i < PAGE_SIZE;i++) {
                        Feature feature = new Feature();
                        feature.setName("====> page " + page + ":name: " + i);
                        feature.setAge("====> page " + page + ":age: " + i );
                        datas.add(feature);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataLoaded(datas);
                        }
                    });
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }
}
