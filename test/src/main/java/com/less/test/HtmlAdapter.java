package com.less.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.less.test.bean.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2018/2/3.
 */

class HtmlAdapter extends BaseAdapter {

    private List<Html> datas;
    private LayoutInflater mInflater;

    public HtmlAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.datas = new ArrayList<>();
    }

    public void addAll(List<Html> list) {
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Html html = datas.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_html, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_id = convertView.findViewById(R.id.tv_id);
            viewHolder.tv_url = convertView.findViewById(R.id.tv_url);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_id.setText(String.valueOf(position + 1));
        viewHolder.tv_url.setText(html.getUrl());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_id;
        TextView tv_url;
    }
}
