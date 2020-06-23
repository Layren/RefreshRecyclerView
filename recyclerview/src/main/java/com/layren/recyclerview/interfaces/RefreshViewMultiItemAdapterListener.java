package com.layren.recyclerview.interfaces;

import com.chad.library.adapter.base.BaseViewHolder;
import com.layren.recyclerview.model.MultiModel;

/**
 * Created by GaoTing on 2018/9/27.
 * <p>
 * Explain:多布局RecyclerView 布局监听
 */
public interface RefreshViewMultiItemAdapterListener<T extends MultiModel> {
    void setHolder(BaseViewHolder holder, T item, int itemType);
}
