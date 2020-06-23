package com.layren.recyclerview.view;

import android.content.Context;
import android.view.View;

import com.layren.recyclerview.adapter.RefreshViewAdapter;
import com.layren.recyclerview.adapter.RefreshViewMultiItemAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/16.
 */

public class FullyGridLayoutManager extends GridLayoutManager {
    private RefreshViewAdapter adapter;
    private RefreshViewMultiItemAdapter multiAdapter;

    public FullyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FullyGridLayoutManager(Context context, int spanCount, RefreshViewAdapter adapter) {
        super(context, spanCount);
        this.adapter = adapter;
    }

    public FullyGridLayoutManager(Context context, int spanCount, RefreshViewMultiItemAdapter multiAdapter) {
        super(context, spanCount);
        this.multiAdapter = multiAdapter;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        try {
            if (adapter != null) {
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int measuredHeight = adapter.getEmptyView().getHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }

            if (multiAdapter != null) {
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int measuredHeight = multiAdapter.getEmptyView().getHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        } catch (Exception e) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }

    }

}
