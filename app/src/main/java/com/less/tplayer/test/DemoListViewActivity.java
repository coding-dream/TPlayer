package com.less.tplayer.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.less.tplayer.Constants;
import com.less.tplayer.R;
import com.less.tplayer.mvp.feature.data.Feature;
import com.less.tplayer.mvp.feature.data.source.FeatureDataSource;
import com.less.tplayer.mvp.feature.data.source.FeatureRepository;
import com.less.tplayer.widget.SuperRefreshLayout;
import com.less.uis.emptylayout.EmptyLayout;

import java.util.List;

/**
 * Created by deeper on 2017/11/27.
 */

public class DemoListViewActivity extends AppCompatActivity {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;

    protected String CACHE_NAME = getClass().getName();

    protected ListView mListView;
    protected SuperRefreshLayout mRefreshLayout;
    protected EmptyLayout mErrorLayout;

    protected MyListAdapter<Feature> mAdapter;
    protected List<Feature> list;

    private View mFooterView;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;

    private int mPage = 1;

    private FeatureRepository mFeatureRepository = new FeatureRepository();
    private static final int PAGE_SIZE = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list_view);
        mListView = (ListView) findViewById(R.id.listView);
        mRefreshLayout = (SuperRefreshLayout) findViewById(R.id.superRefreshLayout);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mErrorLayout = (EmptyLayout) findViewById(R.id.error_layout);
        mRefreshLayout.setSuperRefreshLayoutListener(new SuperRefreshLayout.SuperRefreshLayoutListener() {
            @Override
            public void onRefreshing() {
                doRefresh();
            }

            @Override
            public void onLoadMore() {
                doLoadMore();
            }
        });
        mFooterView = LayoutInflater.from(this).inflate(R.layout.layout_list_view_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DemoListViewActivity.this, "click" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mListView.addFooterView(mFooterView);

        // init Data
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doRefresh();
    }

    private void initData() {
        mAdapter = new MyListAdapter<Feature>(this) {
            @Override
            protected void convert(ViewHolder vh, Feature item, int position) {
                TextView tv_name = vh.getView(R.id.tv_name);
                TextView tv_age = vh.getView(R.id.tv_age);

                tv_name.setText(item.getName());
                tv_age.setText(item.getAge());
            }

            @Override
            protected int getLayoutId(int position, Feature item) {
                return R.layout.item_list_feature;
            }
        };
        mListView.setAdapter(mAdapter);
    }

    private void doRefresh() {
        mFeatureRepository.getDatasByPage(1, new FeatureDataSource.LoadCallback() {
            @Override
            public void onDataLoaded(List<Feature> datas) {
                if(datas != null){
                    mAdapter.clear();
                    mAdapter.addItems(datas);
                    mRefreshLayout.setCanLoadMore();
                    // save to cache
                    // saveToCache();
                    if (datas.size() < PAGE_SIZE) {
                        setFooterType(TYPE_NO_MORE);
                    }
                    mPage = 2;
                }
                //
                if (mAdapter.getDatas().size() > 0) {
                    mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    mRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NODATA);
                }

                mRefreshLayout.onLoadComplete();
            }

            @Override
            public void onDataNotAvailable() {
                setFooterType(TYPE_NET_ERROR);
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);

                mRefreshLayout.onLoadComplete();
            }
        });
    }

    private void doLoadMore() {
        setFooterType(TYPE_LOADING);
        mFeatureRepository.getDatasByPage(mPage, new FeatureDataSource.LoadCallback() {
            @Override
            public void onDataLoaded(List<Feature> datas) {
                if(datas != null){
                    mAdapter.addItems(datas);

                    if (datas.size() < PAGE_SIZE) {
                        setFooterType(TYPE_NO_MORE);
                    }
                    mPage = mPage + 1;
                }
                if (mAdapter.getDatas().size() > 0) {
                    mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    mRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NODATA);
                }

                mRefreshLayout.onLoadComplete();
            }

            @Override
            public void onDataNotAvailable() {
                setFooterType(TYPE_NET_ERROR);
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                mRefreshLayout.onLoadComplete();
            }
        });
    }

    protected void setFooterType(int type) {
        try {
            switch (type) {
                case TYPE_NORMAL:
                case TYPE_LOADING:
                    mFooterText.setText(Constants.FOOTER_TYPE_LOADING);
                    mFooterProgressBar.setVisibility(View.VISIBLE);
                    break;
                case TYPE_NET_ERROR:
                    mFooterText.setText(Constants.FOOTER_TYPE_NET_ERROR);
                    mFooterProgressBar.setVisibility(View.GONE);
                    break;
                case TYPE_ERROR:
                    mFooterText.setText(Constants.FOOTER_TYPE_ERROR);
                    mFooterProgressBar.setVisibility(View.GONE);
                    break;
                case TYPE_NO_MORE:
                    mFooterText.setText(Constants.FOOTER_TYPE_NOT_MORE);
                    mFooterProgressBar.setVisibility(View.GONE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
