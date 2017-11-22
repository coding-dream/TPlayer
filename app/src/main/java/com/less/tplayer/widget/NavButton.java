package com.less.tplayer.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.less.tplayer.R;

/**
 * Created by deeper on 2017/11/21.
 */

public class NavButton extends FrameLayout {
    private Fragment mFragment = null;
    private Class<?> clazz;
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mDot;
    private String mTag;

    public NavButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public NavButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_nav_item, this, true);
        mIconView = (ImageView) findViewById(R.id.nav_iv_icon);
        mTitleView = (TextView) findViewById(R.id.nav_tv_title);
        mDot = (TextView) findViewById(R.id.nav_tv_dot);
    }


    public void bindFragment(@DrawableRes int resId, @StringRes int strId, Class<? extends Fragment> clx) {
        mIconView.setImageResource(resId);
        mTitleView.setText(strId);
        clazz = clx;
        mTag = clazz.getName();
    }

    public Class<?> getClx() {
        return clazz;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public String getNavBtnTag() {
        return mTag;
    }
}
