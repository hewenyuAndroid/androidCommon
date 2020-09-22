package com.newsee.common.adapter.recycler;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import java.util.List;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/9/17 10:41
 * 说明: RecyclerView 单布局通用适配器
 * ====================================
 */
public abstract class SimpleRecyclerAdapter<T> extends MultiRecyclerAdapter<T> {

    protected int mLayoutId;

    protected int mEmptyLayoutId;

    public SimpleRecyclerAdapter(Context context, List<T> dataList, int layoutId) {
        this(context, dataList, layoutId, -1);
    }

    public SimpleRecyclerAdapter(Context context, List<T> dataList, int layoutId, int emptyLayoutId) {
        super(context, dataList);
        this.mLayoutId = layoutId;
        this.mEmptyLayoutId = emptyLayoutId;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return mLayoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T data, int position) {
                SimpleRecyclerAdapter.this.convert(holder, data, position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isValidDataList()) {
            return super.getItemViewType(position);
        }
        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isValidDataList()) {
            return super.onCreateViewHolder(parent, viewType);
        }
        return ViewHolder.createViewHolder(mContext, parent, mEmptyLayoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isValidDataList()) {
            super.onBindViewHolder(holder, position);
        } else {
            doEmptyView(holder);
        }
    }

    /**
     * 空视图回调
     *
     * @param holder
     */
    protected void doEmptyView(ViewHolder holder) {

    }

    @Override
    public int getItemCount() {
        int itemCount = mDataList.size();
        if (isValidDataList()) {
            return itemCount;
        }
        return 1;
    }

    public boolean isEmpty() {
        return mDataList.isEmpty();
    }

    private boolean isValidDataList() {
        return !mDataList.isEmpty() || mEmptyLayoutId == -1;
    }

    public void setEmptyLayout(@LayoutRes int layoutId) {
        this.mEmptyLayoutId = layoutId;
    }

    protected abstract void convert(ViewHolder holder, T data, int position);

}
