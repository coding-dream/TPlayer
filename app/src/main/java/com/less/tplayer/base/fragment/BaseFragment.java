package com.less.tplayer.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.less.tplayer.util.ImageLoader;
import com.less.tplayer.util.LogUtils;

import java.io.Serializable;

import static android.R.attr.id;

/**
 * @author Administrator
 * @date 2017/8/14
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected View mRoot;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;
    private RequestManager mImgLoader;

    /**
     * 懒加载
     */
    private boolean init = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !init) {
            lazyLoadData();
            init = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null) {
                parent.removeView(mRoot);
            }
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;

            if (savedInstanceState != null) {
                onRestartInstance(savedInstanceState);
            }
            initView(mRoot);
            inflateViewStubInCreateView(mRoot);
            initData();
        }
        return mRoot;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImgLoader = null;
        mBundle = null;
    }

    protected <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    /**
     * 获取一个图片加载管理器
     *
     * @return RequestManager
     */
    public synchronized RequestManager getImgLoader() {
        if (mImgLoader == null) {
            mImgLoader = Glide.with(this);
        }
        return mImgLoader;
    }

    protected void setImageFromNet(int viewId, String imageUrl) {
        setImageFromNet(viewId, imageUrl, 0);
    }

    protected void setImageFromNet(int viewId, String imageUrl, int placeholder) {
        ImageView imageView = findView(viewId);
        setImageFromNet(imageView, imageUrl, placeholder);
    }

    protected void setImageFromNet(ImageView imageView, String imageUrl) {
        setImageFromNet(imageView, imageUrl, 0);
    }

    protected void setImageFromNet(ImageView imageView, String imageUrl, int placeholder) {
        ImageLoader.loadImage(getImgLoader(), imageView, imageUrl, placeholder);
    }

    protected void setText(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    protected <T extends View> T setGone(int viewId) {
        T view = findView(id);
        view.setVisibility(View.GONE);
        return view;
    }

    protected <T extends View> T setVisibility(int viewId) {
        T view = findView(id);
        view.setVisibility(View.VISIBLE);
        return view;
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * Fragment onSaveInstanceState() 被调用情况下:savedInstanceState != null
     *
     * @param savedInstanceState
     */
    protected void onRestartInstance(Bundle savedInstanceState){

    }

    /**
     * initView
     *
     * @param mRoot
     */
    protected abstract void initView(View mRoot);

    /**
     * inflate ViewStub in onCreateView(可选)
     *
     * 如果子类没有用到ViewStub 则无需重写此方法
     */
    protected void inflateViewStubInCreateView(View root){
        // nothing to do
    }

    /**
     * Fragment的布局Id
     *
     * @return layoutId
     */
    protected abstract int getLayoutId();

    /**
     * 初始化Fragment的Arguments参数
     *
     * @param bundle
     * @return null
     */
    protected abstract void initBundle(Bundle bundle);

    /**
     * 网络请求
     */
    protected void lazyLoadData(){
        LogUtils.d("====> lazy load <====");
    }
}