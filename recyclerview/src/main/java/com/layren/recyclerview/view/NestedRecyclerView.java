package com.layren.recyclerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.layren.recyclerview.R;
import com.layren.recyclerview.adapter.RefreshViewAdapter;
import com.layren.recyclerview.adapter.RefreshViewMultiItemAdapter;
import com.layren.recyclerview.interfaces.RefreshViewAdapterListener;
import com.layren.recyclerview.interfaces.RefreshViewMultiItemAdapterListener;
import com.layren.recyclerview.model.MultiModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by GaoTing on 2018/11/1.
 * <p>
 * Explain :嵌套 RecyclerView
 */
public class NestedRecyclerView<T> extends RecyclerView {
    private Context context;
    private int count = 1;
    private RefreshViewAdapter<T> adapter;
    private RefreshViewMultiItemAdapter multiAdapter;

    public NestedRecyclerView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public NestedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.NestedRecyclerView);
        count = type.getInteger(R.styleable.NestedRecyclerView_counts, 1);
        type.recycle();
        init();
    }

    /**
     * 初始化NestedRecyclerView
     * <p>
     * 配置LayoutManager(默认纵向)
     */
    private void init() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(context, count);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        setLayoutManager(manager);
        setNestedScrollingEnabled(false);
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(RefreshViewAdapter<T> adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
    }

    /**
     * 获取当前Adapter
     *
     * @return
     */
    @Override
    public BaseQuickAdapter getAdapter() {
        if (adapter != null)
            return adapter;
        if (multiAdapter != null)
            return multiAdapter;
        return null;
    }

    /**
     * 设置Adapter/
     *
     * @param layoutId 布局Id
     * @param listener adapter回调监听
     */
    public void setAdapter(int layoutId, RefreshViewAdapterListener<T> listener) {
        adapter = new RefreshViewAdapter<>(layoutId, listener);
        setAdapter(adapter);
    }

    /**
     * 设置多布局监听
     *
     * @param listener  多布局adapter回调监听
     * @param layoutIds 多布局id数组
     * @param <H>       数据泛型,需继承MultiModel
     */
    public <H extends MultiModel> void setMultiAdapter(RefreshViewMultiItemAdapterListener<H> listener, int... layoutIds) {
        multiAdapter = new RefreshViewMultiItemAdapter<>(listener, layoutIds);
        setAdapter(multiAdapter);
        multiAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            MultiModel model = (MultiModel) getItem(position);
            if (model.getSpanSize() > gridLayoutManager.getSpanCount())
                return gridLayoutManager.getSpanCount();
            return model.getSpanSize();
        });
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

    /**
     * 添加尾布局
     *
     * @param view
     */
    public void addFooter(View view) {
        if (adapter != null)
            adapter.addFooterView(view);
        if (multiAdapter != null)
            multiAdapter.addFooterView(view);
    }

    /**
     * 数据刷新
     */
    public void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (multiAdapter != null)
            multiAdapter.notifyDataSetChanged();
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        adapter.setNewData(list);
    }

    /**
     * 追加数据
     *
     * @param list
     */
    public void addData(List<T> list) {
        adapter.addData(list);
    }

    /**
     * 获取指定位置数据
     *
     * @param position 位置
     * @return
     */
    public T getItem(int position) {
        if (adapter != null)
            return adapter.getItem(position);
        return null;
    }


    /**
     * 获取多布局指定位置数据
     *
     * @param position 位置
     * @return
     */
    public Object getMultiItem(int position) {
        if (multiAdapter != null)
            return multiAdapter.getItem(position);
        return null;
    }

    /**
     * 获取最后一条数据
     *
     * @return
     */
    public Object getLastItem() {
        int last = 0;
        if (adapter != null)
            last = adapter.getItemCount() - 1;
        if (multiAdapter != null)
            last = multiAdapter.getItemCount() - 1;
        if (last >= 0)
            return getItem(last);
        return null;
    }

    /**
     * 设置多布局数据
     *
     * @param list
     */
    public void setMulitData(List<? extends MultiModel> list) {
        multiAdapter.setNewData(list);
    }

    /**
     * 追加多布局数据
     *
     * @param list
     */
    public void addMulitData(List<? extends MultiModel> list) {
        multiAdapter.addData(list);
    }

    /**
     * 移除数据
     *
     * @param position 位置
     */
    public void removeData(int position) {
        if (adapter != null) adapter.remove(position);
        if (multiAdapter != null) multiAdapter.remove(position);
    }

    /**
     * 移除数据
     *
     * @param object 数据对象
     */
    public void removeData(Object object) {
        if (multiAdapter != null) multiAdapter.getData().remove(object);
        if (adapter != null) adapter.getData().remove(object);
    }

    /**
     * 添加分割线
     *
     * @param decoration
     */
    public void addItemDecoration(ItemDecoration decoration) {
        super.addItemDecoration(decoration);
    }

    /**
     * 添加adapter item 点击监听
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener) {
        if (adapter != null)
            adapter.setOnItemClickListener(itemClickListener);
        if (multiAdapter != null)
            multiAdapter.setOnItemClickListener(itemClickListener);
    }

}
