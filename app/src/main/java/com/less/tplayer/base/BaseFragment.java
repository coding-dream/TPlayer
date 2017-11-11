package com.less.tplayer.base;

import android.support.v4.app.Fragment;

/**
 *
 * @author Administrator
 * @date 2017/8/14
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 懒加载
     */
    private boolean init = false;

    protected int mCurrentPage = 1;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !init) {
            requestData();
            init = true;
        }
    }

    protected abstract void requestData();
}