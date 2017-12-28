package com.less.tplayer.mvp.movie;

import com.less.tplayer.R;
import com.less.tplayer.mvp.movie.data.Movie;
import com.less.tplayer.mvp.movie.data.MovieDataSource;
import com.less.tplayer.mvp.movie.data.MovieRepository;

import java.util.List;

/**
 * Created by deeper on 2017/11/25.
 */

public class MoviePresenter implements MovieContract.Presenter {
    private static final int PAGE_SIZE = 30;
    private MovieRepository mMovieRepository;
    private MovieContract.View mView;
    private int mPage = 1;

    public MoviePresenter(MovieRepository movieRepository, MovieFragment fragment) {
        mMovieRepository = movieRepository;
        mView = fragment;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        doRefresh();
    }

    @Override
    public void doRefresh() {
        mMovieRepository.getDatasByPage(1, new MovieDataSource.LoadCallback() {
            @Override
            public void onDataLoaded(List<Movie> datas) {
                if(datas != null){
                    mView.showRefreshSuccess(datas);
                    if (datas.size() < PAGE_SIZE) {
                        mView.showNoMore();
                    }
                    mPage = 2;
                }
                mView.showComplete();
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNetworkError(R.string.state_network_error);
                mView.showComplete();
            }
        });
    }

    @Override
    public void doLoadMore() {
        mMovieRepository.getDatasByPage(mPage, new MovieDataSource.LoadCallback() {
            @Override
            public void onDataLoaded(List<Movie> datas) {
                if(datas != null){
                    mView.showLoadMoreSuccess(datas);
                    if (datas.size() < PAGE_SIZE) {
                        mView.showNoMore();
                    }
                    mPage = mPage + 1;
                }
                mView.showComplete();
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNetworkError(R.string.state_network_error);
                mView.showComplete();
            }
        });
    }
}
