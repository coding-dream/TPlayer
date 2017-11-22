package com.less.tplayer.fragment;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;
import com.less.tplayer.widget.BorderShape;
import com.less.tplayer.widget.NavButton;

/**
 * Created by deeper on 2017/11/21.
 */

public class NavFragment extends BaseFragment {
    NavButton mNavNews;
    NavButton mNavTweet;
    NavButton mNavExplore;
    NavButton mNavMe;

    ImageView mNavPub;
    private Context mContext;
    private int mContainerId;

    private FragmentManager mFragmentManager;
    private NavButton mCurrentNavButton;
    private OnNavReselectListener mOnNavReselectListener;

    public interface OnNavReselectListener {
        void onReselect(NavButton navButton);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onRestartInstance(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View root) {
        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        root.setBackgroundDrawable(layerDrawable);

        mNavNews.bindFragment(R.drawable.tab_icon_new,
                R.string.main_tab_name_news,
                DynamicTabFragment.class);

        mNavTweet.bindFragment(R.drawable.tab_icon_tweet,
                R.string.main_tab_name_tweet,
                TweetViewPagerFragment.class);

        mNavExplore.bindFragment(R.drawable.tab_icon_explore,
                R.string.main_tab_name_explore,
                ExploreFragment.class);

        mNavMe.bindFragment(R.drawable.tab_icon_me,
                R.string.main_tab_name_my,
                UserInfoFragment.class);
        
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    protected void initBundle(Bundle bundle) {

    }
}
