package com.layren.recyclerview.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by GaoTing on 2018/1/2.
 * <p>
 * Explain :分割线
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "ItemDecoration";

    private int dividerHeight;
    private int orientation;
    private Paint dividerPaint;
    private boolean isFrame = true;
    private int countSize = 0;
    private boolean isFirstLine = true;
    private boolean isFirstColumn = true;
    private boolean isLastLine = false;
    private boolean isLastColumn = false;
    private Map<Integer, ItemOffsets> offsetsMap = new HashMap<>();

    public ItemDecoration(int height, int orientation, @ColorInt int color) {
        dividerHeight = height;
        this.orientation = orientation;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
    }

    public ItemDecoration(int height, @ColorInt int color) {
        dividerHeight = height;
        orientation = Gravity.BOTTOM;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
    }

    public ItemDecoration(int height, @ColorInt int color, boolean isFrame) {
        dividerHeight = height;
        orientation = Gravity.BOTTOM;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        this.isFrame = isFrame;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();//列数
        if (spanCount == 1) {
            switch (orientation) {
                case Gravity.TOP:
                    outRect.top = dividerHeight;
                    break;
                case Gravity.LEFT:
                    outRect.left = dividerHeight;
                    break;
                case Gravity.RIGHT:
                    outRect.right = dividerHeight;
                    break;
                case Gravity.BOTTOM:
                    outRect.bottom = dividerHeight;
                    break;
                case Gravity.CENTER:
                    outRect.left = dividerHeight;
                    outRect.top = dividerHeight;
                    outRect.right = dividerHeight;
                    outRect.bottom = dividerHeight;
                    break;
                default:
            }
        } else {
            final int position = parent.getChildAdapterPosition(view);
            final int count = parent.getAdapter().getItemCount();
            int spanSize = layoutManager.getSpanSizeLookup().getSpanSize(position);
            int nextSpanSize = 0;
            if (position < count - 1) {
                nextSpanSize = layoutManager.getSpanSizeLookup().getSpanSize(position + 1);
            }
            if (count == 1) {
                isLastLine = true;
                isLastColumn = true;
            }
            if (position == 0) {
                countSize = spanSize;
            }

            offsetsMap.put(position, new ItemOffsets(isFirstLine, isFirstColumn, isLastLine, isLastColumn));
            if (isFrame) {
                if (isFirstColumn) {
                    //最左边
                    outRect.left = dividerHeight;
                }
                if (isFirstLine) {
                    // 第一行
                    outRect.top = dividerHeight;
                }
                outRect.right = dividerHeight;
                outRect.bottom = dividerHeight;
            } else {
                if (isLastLine && isLastColumn) {
                    // 最后一个，如果也是最右边，那么就不需要偏移
                } else if (isLastLine) {
                    // 最下面一行，只要右边偏移就行
                    outRect.right = dividerHeight;
                } else if (isLastColumn) {
                    // 最右边一列，只要下面偏移就行
                    outRect.bottom = dividerHeight;
                } else {
                    // 其他的话，右边和下面都要偏移
                    outRect.set(0, 0, dividerHeight, dividerHeight);
                }
            }
            countSize += nextSpanSize;
            if (countSize > spanCount) {
                countSize = nextSpanSize;
                isFirstColumn = true;
                isFirstLine = false;
            } else {
                isFirstColumn = false;
            }
            if (countSize == spanCount) {
                isLastColumn = true;
            } else {
                isLastColumn = false;
            }

            if (spanCount >= count - position - 1 && isFirstColumn) {
                int surplusSize = 0;
                for (int i = position + 1; i < count; i++) {
                    surplusSize += layoutManager.getSpanSizeLookup().getSpanSize(i);
                }
                isLastLine = surplusSize <= spanCount;
            }
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();//列数
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }
        if (spanCount == 1) {
            for (int i = 0; i < childCount - 1; i++) {
                View view = parent.getChildAt(i);
                float top = view.getBottom();
                float bottom = view.getBottom() + dividerHeight;
                c.drawRect(0, top, parent.getRight(), bottom, dividerPaint);
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                ItemOffsets itemOffsets = offsetsMap.get(i);
                if (itemOffsets == null) {
                    return;
                }
                View view = parent.getChildAt(i);
                float top = view.getTop();
                float bottom = view.getBottom();
                float left = view.getLeft();
                float right = view.getRight();
                if (isFrame) {
                    if (itemOffsets.isFirstLine) {
                        //第一行
                        c.drawRect(left, top - dividerHeight, right, top, dividerPaint);
                    }
                    if (itemOffsets.isFirstColumn) {
                        //第一列
                        c.drawRect(left - dividerHeight, top, left, bottom, dividerPaint);
                        //左上角
                        c.drawRect(left - dividerHeight, top - dividerHeight, left, top, dividerPaint);
                    }
                    //下边线
                    c.drawRect(left, bottom, right, bottom + dividerHeight, dividerPaint);
                    //右边线
                    c.drawRect(right, top, right + dividerHeight, bottom, dividerPaint);
                    //右下角
                    c.drawRect(right, bottom, right + dividerHeight, bottom + dividerHeight, dividerPaint);
                } else {
                    if (!itemOffsets.isLastLine) {
                        //下边线
                        c.drawRect(left, bottom, right, bottom + dividerHeight, dividerPaint);
                    }
                    if (!itemOffsets.isLastColumn) {
                        //右边线
                        c.drawRect(right, top, right + dividerHeight, bottom, dividerPaint);
                        if (!itemOffsets.isLastLine) {
                            //右下角
                            c.drawRect(right, bottom, right + dividerHeight, bottom + dividerHeight, dividerPaint);
                        }
                    }
                }
            }
        }

    }

    /**
     * 保存Item的位置信息
     */
    private static class ItemOffsets {
        boolean isFirstLine;
        boolean isFirstColumn;
        boolean isLastLine;
        boolean isLastColumn;

        public ItemOffsets(boolean isFirstLine, boolean isFirstColumn, boolean isLastLine, boolean isLastColumn) {
            this.isFirstLine = isFirstLine;
            this.isFirstColumn = isFirstColumn;
            this.isLastLine = isLastLine;
            this.isLastColumn = isLastColumn;
        }
    }


}
