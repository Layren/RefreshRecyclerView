package com.layren.refreshrecyclerview;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.layren.recyclerview.interfaces.OnRefreshListener;
import com.layren.recyclerview.interfaces.RefreshViewAdapterListener;
import com.layren.recyclerview.view.ItemDecoration;
import com.layren.recyclerview.view.RefreshRecyclerView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by GaoTing on 2020/6/22.
 * <p>
 * Explain:
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RefreshRecyclerView<String> refreshRecyclerView = findViewById(R.id.recyclerView);


        //获取 recyclerView;
        refreshRecyclerView.getRecyclerView();
        //获取当前adapter
        refreshRecyclerView.getAdapter();
        //获取指定位置数据
        refreshRecyclerView.getItem(0);

        BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_main, item);
            }

        };
        //设置adapter;
        refreshRecyclerView.setAdapter(adapter);
        //设置adapter,(布局+监听)
        //监听recycleView 泛型与Listener同步,若不添加泛型,则 Item 类型为 Object
        refreshRecyclerView.setAdapter(R.id.tv_item_main, new RefreshViewAdapterListener<String>() {
            @Override
            public void setHolder(BaseViewHolder holder, String item) {
                holder.setText(R.id.tv_item_main, item);
            }
        });


        refreshRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新方法;在下拉触发刷新事件时执行;
            }

            @Override
            public void onLoadMore() {
                //加载更多方法.预加载,在最后一条数据显示时自动执行;

            }
        });
        refreshRecyclerView.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //adapter item 点击事件
            }
        });

        //添加头部View
        refreshRecyclerView.addHeader(new View(this));
        //添加尾部View
        refreshRecyclerView.addFooter(new View(this));
        //添加分割线
        refreshRecyclerView.addItemDecoration(new ItemDecoration(5, 0xff00ff00));
        //隐藏刷新事件
        refreshRecyclerView.hideRefreshView();
        //隐藏刷新事件(是否隐藏)
        refreshRecyclerView.hideRefreshView(false);
        //设置新数据.会覆盖旧数据,默认没有更多数据
        refreshRecyclerView.setData(new ArrayList<String>());
        //,第二参数为 是否还有后续数据,以判断是否执行加载更多方法
        refreshRecyclerView.setData(new ArrayList<String>(), false);
        //添加新数据,第二参数为 是否还有后续数据,以判断是否执行加载更多方法
        refreshRecyclerView.addData(new ArrayList<String>(), false);

    }
}
