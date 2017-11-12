package com.less.tplayer.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.less.swipebacklayout.SwipeBackActivity;
import com.less.tplayer.BuildConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/8/14.
 */

public abstract class BaseActivity extends SwipeBackActivity{
    protected RequestManager mImageLoader;
    private boolean mIsDestroy;
    private final String mPackageNameUmeng = this.getClass().getName();
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        initBundle(getIntent().getExtras());
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        initToolBar();
        initView();
        initData();
        //umeng analytics
        if (BuildConfig.DEBUG) {
            MobclickAgent.setDebugMode(true);
        }
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    /**
     * initToolbar
     */
    protected abstract void initToolBar();

    private void initBundle(Bundle extras) {

    }

    /**
     * 添加下一个Fragment，隐藏当前的Fragment
     * @param frameLayoutId
     * @param fragment
     */
    protected void addFragment(int frameLayoutId,Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    protected void replaceFragment(int framLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(framLayoutId, fragment);
            tx.commit();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.mPackageNameUmeng);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.mPackageNameUmeng);
        MobclickAgent.onPause(this);
    }

    /**
     * get setContentView()'s layoutId
     *
     * @return int
     */
    protected abstract int getLayoutId();

    protected void initView() {

    }

    protected void initData() {

    }

    public <T> T $(int id){
        return (T) findViewById(id);
    }

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }
        return mImageLoader;
    }

    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }

    @Override
    public boolean isDestroyed() {
        return mIsDestroy;
    }
}
