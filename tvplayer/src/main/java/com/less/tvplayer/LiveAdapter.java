package com.less.tvplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2017/12/8.
 */

public abstract class LiveAdapter<T> extends BaseAdapter {
    private List<T> mDatas;
    private Context mContext;

    public LiveAdapter(Context context){
        mContext = context;
        this.mDatas = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_num = convertView.findViewById(R.id.tv_num);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        T item = mDatas.get(position);
        bindData(viewHolder,item,position);
        return convertView;
    }

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

    protected abstract int getLayoutId();

    protected abstract void bindData(ViewHolder viewHolder, T item, int position);

    class ViewHolder {
        protected TextView tv_num;
        protected TextView tv_name;
    }
}
