package com.less.tplayer.base.fragment;

import android.view.View;
import android.view.ViewStub;

import com.less.tplayer.R;
import com.less.uis.TitleBar;

/**
 *
 * @author deeper
 * @date 2017/11/12
 */

public abstract class BaseTitleFragment extends BaseFragment {
    protected TitleBar titleBar;

    @Override
    protected void inflateViewStubInCreateView(View root) {
        super.inflateViewStubInCreateView(root);
        ViewStub stub = (ViewStub) root.findViewById(R.id.lay_content);
        stub.setLayoutResource(getContentLayoutId());
        stub.inflate();
    }

    @Override
    protected void initView(View mRoot) {
        titleBar = mRoot.findViewById(R.id.nav_title_bar);
        titleBar.setTitle(getToolBarTitleRes());
        titleBar.setIcon(getToolBarIconRes());
        titleBar.setIconOnClickListener(getToolbarClickListener());
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_title;
    }

    /**
     * TitleBar icon点击事件
     *
     * @return
     */
    protected View.OnClickListener getToolbarClickListener(){
        return null;
    }

    /**
     * TitleBar icon
     *
     * @return
     */
    protected abstract int getToolBarIconRes();

    /**
     * TitleBar title
     *
     * @return
     */
    protected abstract int getToolBarTitleRes();

    /**
     * 获取ViewStub布局包含的layoutId
     *
     * @return ContentLayoutId
     */
    protected abstract int getContentLayoutId();
}
