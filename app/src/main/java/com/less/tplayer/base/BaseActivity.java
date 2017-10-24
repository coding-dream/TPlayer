package com.less.tplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/8/14.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected RequestManager mImageLoader;
    private boolean mIsDestroy;
    private final String mPackageNameUmeng = this.getClass().getName();
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initBundle(getIntent().getExtras())) {
            setContentView(getContentView());
            initWindow();
            initView();
            initWidget();
            initData();
        } else {
            finish();
        }
        //umeng analytics
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
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

    public abstract int getContentView();

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initWidget() {

    }

    protected void initWindow() {

    }

    protected boolean initBundle(Bundle extras) {
        return true;
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
