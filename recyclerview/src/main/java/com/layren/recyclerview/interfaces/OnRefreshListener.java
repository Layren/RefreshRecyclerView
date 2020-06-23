package com.layren.recyclerview.interfaces;

/**
 * Created by GaoTing on 2019/12/25.
 * <p>
 * Explain: RefreshRecyclerView 刷新和加载更多监听
 */
public interface OnRefreshListener {
    void onRefresh();

    void onLoadMore();
}
