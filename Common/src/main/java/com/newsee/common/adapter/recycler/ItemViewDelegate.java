package com.newsee.common.adapter.recycler;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/9/17 10:29
 * 说明: 多布局
 * ====================================
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
