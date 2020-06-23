package com.layren.recyclerview.interfaces;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by GaoTing on 2018/9/27.
 * <p>
 * Explain : 单布局RecyclerView 监听
 */
public interface RefreshViewAdapterListener<T> {
    void setHolder(BaseViewHolder holder, T item);
}
