package com.less.tplayer.mvp.feature;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.less.tplayer.mvp.feature.data.Feature;

import java.util.List;

/**
 * Created by deeper on 2017/11/25.
 */

public class FeatureAdapter extends RecyclerView.Adapter  {

    protected List<Feature> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
