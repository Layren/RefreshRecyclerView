package com.layren.recyclerview.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.layren.recyclerview.interfaces.RefreshViewAdapterListener;

import androidx.annotation.NonNull;

/**
 * Created by GaoTing on 2017/12/4.
 */

public class RefreshViewAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private RefreshViewAdapterListener<T> listener;

    public RefreshViewAdapter(int layoutResId, @NonNull RefreshViewAdapterListener<T> listener) {
        super(layoutResId);
        this.listener = listener;

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, T item) {
        listener.setHolder(baseViewHolder, item);
    }


}
