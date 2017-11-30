package com.less.tplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.less.tplayer.R;
import com.less.tplayer.base.fragment.BaseTitleFragment;

/**
 * @author deeper
 * @date 2017/11/22
 */

public class DynamicTabFragment extends BaseTitleFragment{

    @Override
    protected int getToolBarIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected int getToolBarTitleRes() {
        return R.string.main_tab_name_news;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_dynamic_tab;
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
                Intent intent = new Intent();
                intent.setClass(getContext(), CaptureActivity.class);
                startActivity(intent);
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
