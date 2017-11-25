package com.less.tplayer.mvp.feature.data.source;

import android.support.annotation.NonNull;

import com.less.tplayer.mvp.feature.data.Feature;

import java.util.List;

/**
 * Created by deeper on 2017/11/24.
 */

public interface FeatureDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<Feature> tasks);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);

    void saveTask(@NonNull Feature task);

    void completeTask(@NonNull Feature task);

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Feature task);

    void activateTask(@NonNull String taskId);

    void clearCompletedTasks();

    void refreshTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);
}
