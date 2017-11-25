package com.less.tplayer.mvp.feature.data.source.remote;

import android.support.annotation.NonNull;

import com.less.tplayer.mvp.feature.data.Feature;
import com.less.tplayer.mvp.feature.data.source.FeatureDataSource;

/**
 * Created by deeper on 2017/11/25.
 */

public class FeatureRemoteDataSource implements FeatureDataSource {

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Feature task) {

    }

    @Override
    public void completeTask(@NonNull Feature task) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Feature task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }
}
