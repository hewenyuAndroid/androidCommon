package com.newsee.common.adapter.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/6/24 10:33
 * 说明: ListView 通用适配器
 * ====================================
 */
public abstract class SimpleListAdapter<T> extends BaseAdapter {

    protected Context mContext;

    protected List<T> mDataList;

    /**
     * 布局Id
     */
    private int mLayoutId;

    public SimpleListAdapter(Context context, List<T> dataList, int layoutId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
        T data = mDataList.get(position);
        convert(holder, data, position);

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T data, int position);

}
