package com.less.tplayer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.less.tplayer.R;
import com.less.tplayer.mvp.common.Injection;
import com.less.tplayer.mvp.feature.FeatureFragment;
import com.less.tplayer.mvp.feature.FeaturePresenter;
import com.less.tplayer.mvp.feature.data.source.FeatureRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2017/11/28.
 */

public class OtherUserHomeActivity extends AppCompatActivity {
    private List<Pair<String, Fragment>> fragments;
    private AppBarLayout layout_appbar;
    private Toolbar toolbar;
    private TextView mNick;

    private ImageView iv_logo_head;
    private TextView tv_logo_nick;

    private View view_divider;
    private TabLayout mTabLayout;
    private ViewPager view_pager;
    private MyOffsetChangerListener mOffsetChangerListener = new MyOffsetChangerListener();

    private class MyOffsetChangerListener implements AppBarLayout.OnOffsetChangedListener {
        boolean isShow = false;
        int mScrollRange = -1;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (mScrollRange == -1) {
                mScrollRange = appBarLayout.getTotalScrollRange();
            }
            if (mScrollRange + verticalOffset == 0) {
                tv_logo_nick.setVisibility(View.VISIBLE);
                iv_logo_head.setVisibility(View.VISIBLE);
                view_divider.setVisibility(View.GONE);

                isShow = true;
            } else if (isShow) {
                tv_logo_nick.setVisibility(View.GONE);
                iv_logo_head.setVisibility(View.GONE);
                view_divider.setVisibility(View.VISIBLE);

                isShow = false;
            }
            mTabLayout.getBackground().setAlpha(Math.round(255 - Math.abs(verticalOffset) / (float) mScrollRange * 255));
        }

        private void resetRange() {
            mScrollRange = -1;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_home);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        layout_appbar = (AppBarLayout) findViewById(R.id.layout_appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mNick = (TextView) findViewById(R.id.tv_nick);
        iv_logo_head = (ImageView) findViewById(R.id.iv_logo_head);
        tv_logo_nick = (TextView) findViewById(R.id.tv_logo_nick);
        view_divider = findViewById(R.id.view_divider);
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        view_pager = (ViewPager) findViewById(R.id.view_pager);

        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.mipmap.btn_back_normal);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout_appbar.addOnOffsetChangedListener(mOffsetChangerListener);

        tv_logo_nick.setText("Limitless");
        iv_logo_head.setImageResource(R.mipmap.ic_launcher);

        mOffsetChangerListener.resetRange();

        requestData();
    }

    private void requestData() {
        // 网络请求成功后初始化 viewPager和fragments
        // .... request

        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView("0", "动弹")));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView("0", "博客")));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView("0", "问答")));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView("0", "讨论")));
        mTabLayout.setVisibility(View.VISIBLE);

        if (fragments == null) {
            fragments = new ArrayList<>();

            FeatureFragment featureFragment1 = new FeatureFragment();
            new FeaturePresenter(Injection.provideRepository(FeatureRepository.class), featureFragment1);

            FeatureFragment featureFragment2 = new FeatureFragment();
            new FeaturePresenter(Injection.provideRepository(FeatureRepository.class), featureFragment2);

            FeatureFragment featureFragment3 = new FeatureFragment();
            new FeaturePresenter(Injection.provideRepository(FeatureRepository.class), featureFragment3);

            FeatureFragment featureFragment4 = new FeatureFragment();
            new FeaturePresenter(Injection.provideRepository(FeatureRepository.class), featureFragment4);

            fragments.add(new Pair<String, Fragment>(String.format("%s\n动弹", 0),featureFragment1));
            fragments.add(new Pair<String, Fragment>(String.format("%s\n博客", 0),featureFragment2));
            fragments.add(new Pair<String, Fragment>(String.format("%s\n问答", 0),featureFragment3));
            fragments.add(new Pair<String, Fragment>(String.format("%s\n讨论", 0),featureFragment4));

            view_pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position).second;
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return fragments.get(position).first;
                }
            });

            mTabLayout.setupWithViewPager(view_pager);

            // 注意: 这有一个TabLayout的坑: TabLayout will remove up all tabs after setted up view pager
            // so we set it again
            // 这里的100,1000,889,33等都是模拟请求的数据.
            mTabLayout.getTabAt(0).setCustomView(getTabView("100", "动弹"));
            mTabLayout.getTabAt(1).setCustomView(getTabView("1000", "博客"));
            mTabLayout.getTabAt(2).setCustomView(getTabView("889", "问答"));
            mTabLayout.getTabAt(3).setCustomView(getTabView("33", "讨论"));
        } else { // when request user detail info successfully
            setupTabText(mTabLayout.getTabAt(0), "100");
            setupTabText(mTabLayout.getTabAt(1), "1000");
            setupTabText(mTabLayout.getTabAt(2), "889");
            setupTabText(mTabLayout.getTabAt(3), "33");
        }
    }

    private void setupTabText(TabLayout.Tab tab, String tv_count) {
        View view = tab.getCustomView();
        if (view == null) {
            return;
        }
        TabViewHolder holder = (TabViewHolder) view.getTag();
        holder.tv_count.setText(tv_count);
    }

    private View getTabView(String tv_count, String tv_name) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_other_user, mTabLayout, false);
        TabViewHolder holder = new TabViewHolder(view);
        holder.tv_count.setText(tv_count);
        holder.tv_name.setText(tv_name);
        view.setTag(holder);
        return view;
    }
    private static class TabViewHolder {
        private TextView tv_count;
        private TextView tv_name;

        private TabViewHolder(View view) {
            tv_count = (TextView) view.findViewById(R.id.tv_count);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
