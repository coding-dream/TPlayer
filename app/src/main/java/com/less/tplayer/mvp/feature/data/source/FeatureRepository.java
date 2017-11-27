package com.less.tplayer.mvp.feature.data.source;

import android.os.Handler;
import android.os.Looper;

import com.less.tplayer.mvp.feature.data.Feature;
import com.less.tplayer.mvp.feature.data.source.local.FeatureLocalDataSource;
import com.less.tplayer.mvp.feature.data.source.remote.FeatureRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2017/11/25.
 */

public class FeatureRepository implements FeatureDataSource {

    private static FeatureRepository INSTANCE = null;

    private final FeatureDataSource mRetemoDataSource;

    private final FeatureDataSource mLocalDataSource;

    private Handler handler = new Handler(Looper.getMainLooper());

    public FeatureRepository() {
        this.mLocalDataSource = new FeatureLocalDataSource();
        this.mRetemoDataSource = new FeatureRemoteDataSource();
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
