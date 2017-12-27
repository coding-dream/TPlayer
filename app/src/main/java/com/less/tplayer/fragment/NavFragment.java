package com.less.tplayer.fragment;

import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseFragment;
import com.less.tplayer.ui.PubActivity;
import com.less.tplayer.widget.BorderShape;
import com.less.tplayer.widget.NavButton;

import java.util.List;

/**
 * Created by deeper on 2017/11/21.
 */

public class NavFragment extends BaseFragment implements View.OnClickListener {
    private NavButton navItemNews;
    private NavButton navItemTweet;
    private NavButton navItemExplore;
    private NavButton navItemMe;

    ImageView mNavPub;
    private int mContainerId;

    private FragmentManager mFragmentManager;
    private NavButton mCurrentNavButton;
    private OnNavReselectListener mOnNavReselectListener;

    public NavFragment() {
        // Required empty public constructor
    }
    public interface OnNavReselectListener {
        /**
         * navButton被重复选中时触发
         * @param navButton
         */
        void onReselect(NavButton navButton);
    }

    public void setup(FragmentManager fragmentManager, int contentId, OnNavReselectListener listener) {
        this.mFragmentManager = fragmentManager;
        this.mContainerId = contentId;
        this.mOnNavReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(navItemNews);
    }

    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0) {
            return;
        }
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            // 清除除自己以外的所有fragments
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit) {
            transaction.commitNow();
        }
    }

    @Override
    protected void initData() {
        // nothing to do
    }

    @Override
    protected void initView(View view) {
        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        view.setBackgroundDrawable(layerDrawable);
        navItemNews = view.findViewById(R.id.nav_item_news);
        navItemTweet = view.findViewById(R.id.nav_item_tweet);
        navItemExplore = view.findViewById(R.id.nav_item_explore);
        navItemMe = view.findViewById(R.id.nav_item_me);

        mNavPub =  view.findViewById(R.id.nav_item_tweet_pub);

        navItemNews.bindFragment(R.drawable.tab_icon_new,
                R.string.main_tab_name_news,
                TabHomeFragment.class);

        navItemTweet.bindFragment(R.drawable.tab_icon_tweet,
                R.string.main_tab_name_tweet,
                TabMoviePagerFragment.class);

        navItemExplore.bindFragment(R.drawable.tab_icon_explore,
                R.string.main_tab_name_explore,
                TabTVFragment.class);

        navItemMe.bindFragment(R.drawable.tab_icon_me,
                R.string.main_tab_name_my,
                TabUserFragment.class);
        navItemNews.setOnClickListener(this);
        navItemTweet.setOnClickListener(this);
        navItemExplore.setOnClickListener(this);
        navItemMe.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        // nothing to do
    }

    @Override
    public void onClick(View v) {
        if (v instanceof NavButton) {
            NavButton nav = (NavButton) v;
            doSelect(nav);
        } else if (v.getId() == R.id.nav_item_tweet_pub) {
            PubActivity.show(getContext());
        }
    }

    private void doSelect(NavButton selectNav) {
        NavButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == selectNav) {
                if (mOnNavReselectListener != null) {
                    mOnNavReselectListener.onReselect(oldNavButton);
                }
                return;
            }
            oldNavButton.setSelected(false);
        }
        selectNav.setSelected(true);
        switchFragment(oldNavButton, selectNav);
        mCurrentNavButton = selectNav;
    }

    private void switchFragment(NavButton oldNavButton, NavButton selectNav) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                transaction.detach(oldNavButton.getFragment());
            }
        }
        if (selectNav != null) {
            if (selectNav.getFragment() != null) {
                transaction.attach(selectNav.getFragment());
            } else {
                // Fragment.instantiate工具方法,再也不用自己newInstance().
                Fragment fragment = Fragment.instantiate(mContext, selectNav.getClx().getName(), null);
                transaction.add(mContainerId, fragment, selectNav.getNavBtnTag());
                selectNav.setFragment(fragment);
            }
        }
        transaction.commit();
    }
}
