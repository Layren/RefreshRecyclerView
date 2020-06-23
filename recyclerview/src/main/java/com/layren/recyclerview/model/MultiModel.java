package com.layren.recyclerview.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by GaoTing on 2018/9/27.
 * <p>
 * explain :
 */
public class MultiModel<T> extends AbstractExpandableItem<T> implements MultiItemEntity {
    //数据类型
    private int itemType;
    // item宽度.默认为1
    private int spanSize = 1;
    //折叠层级
    private int level = 1;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
