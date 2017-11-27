package com.less.tplayer.test;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2017/11/27.
 */

public abstract class MyListAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    private List<T> mDatas;

    public MyListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = new ArrayList<T>();
    }

    @Override
    public int getCount() {
       return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = this.getItem(position);
        int layoutId = getLayoutId(position, item);
        ViewHolder vh = ViewHolder.getViewHolder(mInflater, convertView, parent, layoutId, position);
        convert(vh, item, position);
        return vh.getConvertView();
    }

    protected abstract void convert(ViewHolder vh, T item, int position);

    protected abstract int getLayoutId(int position, T item);

    public void addItems(List<T> items) {
        if (items != null) {
            mDatas.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mDatas.clear();
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public static class ViewHolder {
        private SparseArray<View> mViews;
        private int mLayoutId;
        private View mConvertView;
        private int mPosition;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId, int position) {
            this.mViews = new SparseArray<View>();
            this.mPosition = position;
            this.mLayoutId = layoutId;
            this.mConvertView = inflater.inflate(layoutId, parent, false);
            this.mConvertView.setTag(this);
        }

        /**
         * 获取一个viewHolder
         *
         * @param convertView view
         * @param parent      parent view
         * @param layoutId    布局资源id
         * @param position    索引
         * @return
         */
        public static ViewHolder getViewHolder(LayoutInflater inflater, View convertView, ViewGroup parent, int layoutId, int position) {
            boolean needCreateView = false;
            ViewHolder vh = null;
            if (convertView == null) {
                needCreateView = true;
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if (vh != null && (vh.mLayoutId != layoutId)) {
                needCreateView = true;
            }
            if (needCreateView) {
                return new ViewHolder(inflater, parent, layoutId, position);
            }
            return (ViewHolder) convertView.getTag();
        }

        public int getPosition() {
            return this.mPosition;
        }

        public int getLayoutId() {
            return mLayoutId;
        }

        // 通过一个viewId来获取一个view
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        // 返回viewHolder的容器类
        public View getConvertView() {
            return this.mConvertView;
        }
    }
}
