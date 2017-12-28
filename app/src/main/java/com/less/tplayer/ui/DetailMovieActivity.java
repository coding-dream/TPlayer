package com.less.tplayer.ui;

import android.os.Bundle;

import com.less.tplayer.R;
import com.less.tplayer.base.activity.BaseActivity;
import com.less.tplayer.mvp.movie.data.Movie;
import com.less.tplayer.util.LogUtils;

/**
 *
 * @author deeper
 * @date 2017/12/28
 */

public class DetailMovieActivity extends BaseActivity {

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void initBundle(Bundle extras) {
        super.initBundle(extras);
        Movie movie = (Movie) extras.getSerializable("movie");
        LogUtils.d(movie.toString());
    }
}
