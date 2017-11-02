package com.less.tplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/8/14.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected LayoutInflater mInflater;
    public boolean visible ;
    protected RequestManager mImageLoader;
    private boolean mIsDestroy;
    private final String mPackageNameUmeng = this.getClass().getName();
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = getLayoutInflater();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        initView();
        initData();
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
        visible = true;
        MobclickAgent.onPageStart(this.mPackageNameUmeng);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
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

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 令子Activity initToolbar()之后，点击左上方按钮有效返回
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
