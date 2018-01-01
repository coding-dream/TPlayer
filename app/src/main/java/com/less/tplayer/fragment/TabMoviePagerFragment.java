package com.less.tplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.less.tplayer.R;
import com.less.tplayer.base.adapter.BaseViewPagerAdapter;
import com.less.tplayer.base.fragment.BaseViewPagerFragment;
import com.less.tplayer.bean.PagerInfo;
import com.less.tplayer.interfaces.IFragmentReSelected;
import com.less.tplayer.mvp.movie.MovieFragment;
import com.less.tplayer.util.MovieConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author deeper
 * @date 2017/11/22
 */

public class TabMoviePagerFragment extends BaseViewPagerFragment implements IFragmentReSelected {

    @Override
    protected int getToolBarIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected int getToolBarTitleRes() {
        return R.string.main_tab_name_tweet;
    }

    @Override
    protected List<PagerInfo> getPagers() {
        List<PagerInfo> list = new LinkedList<>();

        MovieConfig movieConfig = MovieConfig.create();
        Map<String,String> catMap = movieConfig.getCatMap();
        for (Map.Entry<String, String> entry : catMap.entrySet()) {
            list.add(new PagerInfo(entry.getValue(),MovieFragment.class,createFragArgs(entry.getKey())));
        }
        return list;
    }

    private Bundle createFragArgs(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString(MovieFragment.CATALOG_KEY, catalog);
        return bundle;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initBundle(Bundle bundle) {

    }

    @Override
    protected View.OnClickListener getToolbarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "you click toolbar", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void callReSelect() {
        if (mBaseViewPager != null) {
            BaseViewPagerAdapter pagerAdapter = (BaseViewPagerAdapter) mBaseViewPager.getAdapter();
            Fragment fragment = pagerAdapter.getCurrentFrag();
            if (fragment != null && fragment instanceof IFragmentReSelected) {
                ((IFragmentReSelected) fragment).callReSelect();
            }
        }
    }
}
