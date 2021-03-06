package com.layren.recyclerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.layren.recyclerview.R;
import com.layren.recyclerview.adapter.RefreshViewAdapter;
import com.layren.recyclerview.adapter.RefreshViewMultiItemAdapter;
import com.layren.recyclerview.interfaces.OnRefreshListener;
import com.layren.recyclerview.interfaces.RefreshViewAdapterListener;
import com.layren.recyclerview.interfaces.RefreshViewMultiItemAdapterListener;
import com.layren.recyclerview.model.MultiModel;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Created by Administrator on 2017/12/4.
 */

public class RefreshRecyclerView<T> extends LinearLayout {
    private View view;
    private SwipeRefreshLayout reLayout;
    private RecyclerView reView;

    private Context context;
    private OnRefreshListener listener;
    private RefreshViewAdapter<T> adapter;
    private RefreshViewMultiItemAdapter multiAdapter;
    private int count = 1;
    private boolean hideRefresh = false;

    public RefreshRecyclerView(Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.include_recyclerview, this);
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.include_recyclerview, this);
        TypedArray type = context.obtainStyledAttributes(attr, R.styleable.RefreshRecyclerView);
        count = type.getInteger(R.styleable.RefreshRecyclerView_counts, 1);
        hideRefresh = type.getBoolean(R.styleable.RefreshRecyclerView_hideRefresh, false);
        type.recycle();
        init();
    }


    /**
     * 获取 RecyclerView
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return reView;
    }

    /**
     * 获取当前Adapter
     *
     * @return
     */
    public BaseQuickAdapter getAdapter() {
        if (adapter != null)
            return adapter;
        if (multiAdapter != null)
            return multiAdapter;
        return null;
    }

    /**
     * 初始化..
     * 配置RecyclerView,LayoutManager
     */
    private void init() {
        reLayout = view.findViewById(R.id.SwipeRefreshLayout_RefreshRecyclerView);
        reView = view.findViewById(R.id.RecyclerView_RefreshRecyclerView);
        if (hideRefresh) hideRefreshView();
        reLayout.setColorSchemeColors(0xffffff00, 0xffff00ff, 0xff00ffff);
        reLayout.setOnRefreshListener(() -> {
            if (listener != null)
                listener.onRefresh();
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(context, count, adapter);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        reView.setLayoutManager(manager);
    }

    /**
     * 设置Adapter
     *
     * @param layoutId 布局Id
     * @param listener adapter 回调监听
     */
    public void setAdapter(int layoutId, RefreshViewAdapterListener<T> listener) {
        adapter = new RefreshViewAdapter<>(layoutId, listener);
        setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> {
            if (RefreshRecyclerView.this.listener != null && adapter.isLoadMoreEnable())
                RefreshRecyclerView.this.listener.onLoadMore();
        }, reView);
    }

    /**
     * 设置adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        reView.setAdapter(adapter);
    }

    /**
     * 设置多布局adapter
     *
     * @param listener  adapter 回调监听
     * @param layoutIds 多布局id数组
     */
    public <H extends MultiModel> void setMultiAdapter(RefreshViewMultiItemAdapterListener<H> listener, int... layoutIds) {
        multiAdapter = new RefreshViewMultiItemAdapter<>(listener, layoutIds);
        setAdapter(multiAdapter);
        multiAdapter.setEnableLoadMore(true);
        multiAdapter.setOnLoadMoreListener(() -> {
            if (RefreshRecyclerView.this.listener != null)
                RefreshRecyclerView.this.listener.onLoadMore();
        }, reView);
        multiAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            MultiModel model = (MultiModel) getMultiItem(position);
            if (model.getSpanSize() > gridLayoutManager.getSpanCount())
                return gridLayoutManager.getSpanCount();
            return model.getSpanSize();
        });
    }

    /**
     * 设置刷新和加载更多监听
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }


    /**
     * 添加头布局
     *
     * @param view
     */
    public void addHeader(View view) {
        if (adapter != null)
            adapter.addHeaderView(view);
        if (multiAdapter != null)
            multiAdapter.addHeaderView(view);
    }

    public void addFooter(View view) {
        if (adapter != null)
            adapter.addFooterView(view);
        if (multiAdapter != null)
            multiAdapter.addFooterView(view);
    }


    public void setRefreshing(final boolean refreshing) {
        if (refreshing && listener != null) {
            listener.onRefresh();
        }
        reLayout.post(() -> reLayout.setRefreshing(refreshing));
    }

    public void setLoadMoreView(LoadMoreView view) {
        if (adapter != null)
            adapter.setLoadMoreView(view);
        if (multiAdapter != null)
            multiAdapter.setLoadMoreView(view);
    }

    public void setSchemeColor(int... colors) {
        reLayout.setColorSchemeColors(colors);
    }

    public void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (multiAdapter != null)
            multiAdapter.notifyDataSetChanged();
    }

    public void setData(List<T> list, boolean haveMore) {
        adapter.setNewData(list);
        if (haveMore)
            adapter.loadMoreComplete();
        else
            adapter.loadMoreEnd(true);
    }

    public void setData(List<T> list) {
        setData(list, false);
    }

    public void addData(List<T> list, boolean haveMore) {
        adapter.addData(list);
        if (haveMore)
            adapter.loadMoreComplete();
        else
            adapter.loadMoreEnd();
    }

    public T getItem(int position) {
        if (adapter != null)
            return adapter.getItem(position);
        return null;
    }

    public Object getMultiItem(int position) {
        if (multiAdapter != null)
            return multiAdapter.getItem(position);
        return null;
    }


    public void setMultiData(List<? extends MultiModel> list) {
        setMultiData(list, false);
    }

    public void setMultiData(List<? extends MultiModel> list, boolean haveMore) {
        multiAdapter.setNewData(list);
        if (haveMore) multiAdapter.loadMoreComplete();
        else
            multiAdapter.loadMoreEnd(true);

    }

    public void addMultiData(List<? extends MultiModel> list, boolean haveMore) {
        multiAdapter.addData(list);
        if (haveMore)
            multiAdapter.loadMoreComplete();
        else
            multiAdapter.loadMoreEnd();
    }

    public void removeData(int position) {
        if (adapter != null) adapter.remove(position);
        if (multiAdapter != null) multiAdapter.remove(position);
    }

    public void removeData(Object object) {
        if (multiAdapter != null) multiAdapter.getData().remove(object);
        if (adapter != null) adapter.getData().remove(object);

    }

    //添加分割线
    public void addItemDecoration(ItemDecoration decoration) {
        reView.addItemDecoration(decoration);
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener) {
        if (adapter != null)
            adapter.setOnItemClickListener(itemClickListener);
        if (multiAdapter != null)
            multiAdapter.setOnItemClickListener(itemClickListener);
    }


    public void hideRefreshView() {
        hideRefreshView(true);
    }

    public void hideRefreshView(boolean hide) {
        reLayout.setEnabled(!hide);
    }

}
