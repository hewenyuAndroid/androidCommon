package com.newsee.common.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/9/17 10:34
 * 说明: RecyclerView 多布局通用适配器
 * ====================================
 */
public class MultiRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;

    protected List<T> mDataList;

    protected ItemViewDelegateManager mItemViewDelegateManager;

    /**
     * 点击事件防抖时间
     */
    protected long mDelay = 800;
    /**
     * 首次点击的时间
     */
    private long mClickTime = -1;

    public MultiRecyclerAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) {
            return super.getItemViewType(position);
        }
        return mItemViewDelegateManager.getItemViewType(mDataList.get(position), position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onCreateViewHolder(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDataList.get(position));
    }

    public void onCreateViewHolder(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                if (mClickTime != -1 && currentTime - mClickTime < mDelay) {
                    return;
                }
                mClickTime = currentTime;
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemLongClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        int itemCount = mDataList.size();
        return itemCount;
    }


    public List<T> getDataList() {
        return mDataList;
    }

    public MultiRecyclerAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiRecyclerAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    // region ------------------------------------ 点击事件回调 ------------------------------------

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);

    }

    // endregion -----------------------------------------------------------------------------------

}
