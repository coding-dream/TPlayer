package com.less.tplayer.base.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.less.tplayer.bean.PagerInfo;

import java.util.List;

/**
 * Created by deeper on 2017/11/23.
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {
    private List<PagerInfo> mInfoList;
    private Fragment mCurFragment;
    private Context mContext;

    public BaseViewPagerAdapter(Context context,FragmentManager fm, List<PagerInfo> infoList) {
        super(fm);
        this.mContext = context;
        mInfoList = infoList;
    }

    public Fragment getCurrentFrag(){
        return mCurFragment;
    }

    @Override
    public Fragment getItem(int position) {
        PagerInfo info = mInfoList.get(position);
        return Fragment.instantiate(mContext, info.getClx().getName(), info.getArgs());
    }

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurFragment = (Fragment) object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mInfoList.get(position).getTitle();
    }

    @Override
    public int getItemPosition(Object object) {
        // FragmentPagerAdapter 的坑,notifyDataChanged不刷新的问题,fucking
        // 两个值: 1. POSITION_UNCHANGED, 2. POSITION_NONE, todo read source[ F4 查看某个抽象类的实现者 ]
        return PagerAdapter.POSITION_NONE;
    }
}
