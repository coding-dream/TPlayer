package com.less.tplayer.mvp.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;
import com.less.tplayer.interfaces.IFragmentReSelected;
import com.less.tplayer.mvp.common.Injection;
import com.less.tplayer.mvp.feature.FeatureAdapter;
import com.less.tplayer.mvp.movie.data.Movie;
import com.less.tplayer.mvp.movie.data.MovieRepository;
import com.less.tplayer.ui.DetailMovieActivity;
import com.less.tplayer.widget.RecyclerRefreshLayout;

import java.util.List;

/**
 * Created by deeper on 2017/11/23.
 */

public class MovieFragment extends BaseFragment implements IFragmentReSelected,MovieContract.View {

    public static final String CATALOG_KEY = "key";

    private MovieContract.Presenter mPresenter;

    protected RecyclerRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected MovieAdapter mAdapter;
    private String type;

    public MovieFragment(){
        new MoviePresenter(Injection.provideRepository(MovieRepository.class), this);
    }

    @Override
    protected void initData() {
        // nothing to do
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mPresenter.start();;
            }
        });
    }

    @Override
    protected void initView(View mRoot) {
        mRefreshLayout = (RecyclerRefreshLayout) mRoot.findViewById(R.id.refreshLayout);
        mRefreshLayout.setSuperRefreshLayoutListener(new RecyclerRefreshLayout.SuperRefreshLayoutListener() {
            @Override
            public void onRefreshing() {
                mAdapter.setState(FeatureAdapter.STATE_HIDE, true);
                mPresenter.doRefresh();
            }

            @Override
            public void onLoadMore() {
                mAdapter.setState(FeatureAdapter.STATE_LOADING, true);
                mPresenter.doLoadMore();
            }
        });
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.recyclerView);
        mAdapter = new MovieAdapter(getContext(),FeatureAdapter.ONLY_FOOTER);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                // handle click
                Movie movie = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(mContext, DetailMovieActivity.class);
                startActivity(intent);
            }
        });
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_movie;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        type = getBundleSerializable(CATALOG_KEY);
    }

    public String getType() {
        return type;
    }

    @Override
    public void callReSelect() {

    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRefreshSuccess(List<Movie> datas) {
        mAdapter.resetItem(datas);
    }

    @Override
    public void showLoadMoreSuccess(List<Movie> datas) {
        mAdapter.addAll(datas);
    }

    @Override
    public void showNoMore() {
        mAdapter.setState(FeatureAdapter.STATE_NO_MORE, true);
    }

    @Override
    public void showComplete() {
        mRefreshLayout.onComplete();
    }

    @Override
    public void showNetworkError(int strId) {
        mAdapter.setState(FeatureAdapter.STATE_INVALID_NETWORK, true);
    }
}
