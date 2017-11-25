package com.less.tplayer.mvp.feature;

import android.content.Context;
import android.support.annotation.NonNull;

import com.less.tplayer.mvp.feature.data.source.FeatureRepository;

/**
 * Created by deeper on 2017/11/24.
 */

public class Injection {
    public static FeatureRepository provideRepository(@NonNull Context context) {
//        Database database = Database.getInstance(context);
//        return FeatureRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
//                TasksLocalDataSource.getInstance(new AppExecutors(),database.taskDao()));
        return null;
    }
}
