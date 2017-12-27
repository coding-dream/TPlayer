package com.less.tplayer.mvp.movie;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;
import com.less.tplayer.interfaces.IFragmentReSelected;
import com.less.tplayer.mvp.common.Injection;
import com.less.tplayer.mvp.feature.FeatureAdapter;
import com.less.tplayer.mvp.feature.data.Feature;
import com.less.tplayer.mvp.movie.data.MovieRepository;
import com.less.tplayer.widget.RecyclerRefreshLayout;

import java.util.List;

/**
 * Created by deeper on 2017/11/23.
 */

public class MovieFragment extends BaseFragment implements IFragmentReSelected,MovieContract.View {

    public static final String CATALOG_KEY = "key";
    public static final int CATALOG_NEW = 1;
    public static final int CATALOG_HOT = 2;
    public static final int CATALOG_MYSELF = 3;

    private int type;

    private MovieContract.Presenter mPresenter;

    protected RecyclerRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected MovieAdapter mAdapter;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                // handle click
                Toast.makeText(mContext, "position:" + position + " itemId: " + itemId, Toast.LENGTH_SHORT).show();
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
        System.out.println("type is " + type);
    }

    @Override
    public void callReSelect() {
        Toast.makeText(mContext, "重复选中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRefreshSuccess(List<Feature> datas) {
        mAdapter.resetItem(datas);
    }

    @Override
    public void showLoadMoreSuccess(List<Feature> datas) {
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
