/*******************************************************************************
 * Copyright 2012 huewu.yang <hueuw.yang@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.starbaby_03.main;

import com.example.starbaby_03.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;


/**
 * @author huewu.ynag
 * @date 2012-11-06
 */
public class MultiColumnListView extends PLA_ListView {

    @SuppressWarnings("unused")
    private static final String TAG = "MultiColumnListView";

    private static final int DEFAULT_COLUMN_NUMBER = 2;

    private int mColumnNumber = 2;
    private Column[] mColumns = null;
    private Column mFixedColumn = null; // column for footers & headers.
    private SparseIntArray mItems = new SparseIntArray();

    private int mColumnPaddingLeft = 0;
    private int mColumnPaddingRight = 0;

    public MultiColumnListView(Context context) {
        super(context);
        init(null);
    }

    public MultiColumnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private Rect mFrameRect = new Rect();

    private void init(AttributeSet attrs) {
        getWindowVisibleDisplayFrame(mFrameRect);

        if (attrs == null) {
            mColumnNumber = (DEFAULT_COLUMN_NUMBER); // 默认列号是2
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinterestLikeAdapterView);

            int landColNumber = a.getInteger(R.styleable.PinterestLikeAdapterView_plaLandscapeColumnNumber, 3);
            int defColNumber = a.getInteger(R.styleable.PinterestLikeAdapterView_plaColumnNumber, 2);

            if (mFrameRect.width() > mFrameRect.height() && landColNumber != -1) {
                mColumnNumber = (landColNumber);
            } else if (defColNumber != -1) {
                mColumnNumber = (defColNumber);
            } else {
                mColumnNumber = (DEFAULT_COLUMN_NUMBER);
            }

            mColumnPaddingLeft = a.getDimensionPixelSize(R.styleable.PinterestLikeAdapterView_plaColumnPaddingLeft, 0);
            mColumnPaddingRight = a.getDimensionPixelSize(R.styleable.PinterestLikeAdapterView_plaColumnPaddingRight, 0);
            a.recycle();
        }

        mColumns = new Column[getColumnNumber()];
        for (int i = 0; i < getColumnNumber(); ++i)
            mColumns[i] = new Column(i);

        mFixedColumn = new FixedColumn();
    }

    // /////////////////////////////////////////////////////////////////////
    // Override Methods...
    // /////////////////////////////////////////////////////////////////////

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // TODO 适配器状态可能被改变。我应该做些什么在这里……
    }

    private int columnWidth;

    public int getColumnWidth() {
        return columnWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        columnWidth = (getMeasuredWidth() - mListPadding.left - mListPadding.right - mColumnPaddingLeft - mColumnPaddingRight)
                / getColumnNumber();

        for (int index = 0; index < getColumnNumber(); ++index) {
            mColumns[index].mColumnWidth = columnWidth;
            mColumns[index].mColumnLeft = mListPadding.left + mColumnPaddingLeft + columnWidth * index;
        }

        mFixedColumn.mColumnLeft = mListPadding.left;
        mFixedColumn.mColumnWidth = getMeasuredWidth();
    }

    @Override
    protected void onMeasureChild(View child, int position, int widthMeasureSpec, int heightMeasureSpec) {
        if (isFixedView(child))
            child.measure(widthMeasureSpec, heightMeasureSpec);
        else
            child.measure(MeasureSpec.EXACTLY | getColumnWidth(position), heightMeasureSpec);
    }

    @Override
    protected int modifyFlingInitialVelocity(int initialVelocity) {
        return initialVelocity / getColumnNumber();
    }

    @Override
    protected void onItemAddedToList(int position, boolean flow) {
        super.onItemAddedToList(position, flow);

        if (isHeaderOrFooterPosition(position) == false) {
            Column col = getNextColumn(flow, position);
            mItems.append(position, col.getIndex());
        }
    }

    @Override
    protected void onLayoutSync(int syncPos) {
        for (Column c : mColumns) {
            c.save();
        }
    }

    @Override
    protected void onLayoutSyncFinished(int syncPos) {
        for (Column c : mColumns) {
            c.clear();
        }
    }

    @Override
    protected void onAdjustChildViews(boolean down) {

        int firstItem = getFirstVisiblePosition();
        if (down == false && firstItem == 0) {
            final int firstColumnTop = mColumns[0].getTop();
            for (Column c : mColumns) {
                final int top = c.getTop();
                // 对齐所有列的顶部为0的列。
                c.offsetTopAndBottom(firstColumnTop - top);
            }
        }
        super.onAdjustChildViews(down);
    }

    @Override
    protected int getFillChildBottom() {
        // 返回值最小底部。
        // 为了确定填补了……(计算低于空间)
        int result = Integer.MAX_VALUE;
        for (Column c : mColumns) {
            int bottom = c.getBottom();
            result = result > bottom ? bottom : result;
        }
        return result;
    }

    @Override
    protected int getFillChildTop() {
        // 找到最大的列。
        int result = Integer.MIN_VALUE;
        for (Column c : mColumns) {
            int top = c.getTop();
            result = result < top ? top : result;
        }
        return result;
    }

    @Override
    protected int getScrollChildBottom() {
        // 返回值最大底部。
        // 检查滚动区……
        int result = Integer.MIN_VALUE;
        for (Column c : mColumns) {
            int bottom = c.getBottom();
            result = result < bottom ? bottom : result;
        }
        return result;
    }

    @Override
    protected int getScrollChildTop() {
        //找到最大的列。
        int result = Integer.MAX_VALUE;
        for (Column c : mColumns) {
            int top = c.getTop();
            result = result > top ? top : result;
        }
        return result;
    }

    @Override
    protected int getItemLeft(int pos) {

        if (isHeaderOrFooterPosition(pos))
            return mFixedColumn.getColumnLeft();

        return getColumnLeft(pos);
    }

    @Override
    protected int getItemTop(int pos) {

        if (isHeaderOrFooterPosition(pos))
            return mFixedColumn.getBottom(); // 页脚视图应放置下面最后一列。

        int colIndex = mItems.get(pos, -1);
        if (colIndex == -1)
            return getFillChildBottom();

        return mColumns[colIndex].getBottom();
    }

    @Override
    protected int getItemBottom(int pos) {

        if (isHeaderOrFooterPosition(pos))
            return mFixedColumn.getTop(); // 　标题视图应该是上面的地方第一列项目。

        int colIndex = mItems.get(pos, -1);
        if (colIndex == -1)
            return getFillChildTop();

        return mColumns[colIndex].getTop();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Private Methods...
    // ////////////////////////////////////////////////////////////////////////////

    //如果流是真实流动,顶部边缘对齐到y。如果为假,底部边缘对齐 y
    private Column getNextColumn(boolean flow, int position) {

        // 我们已经有这个项目……
        int colIndex = mItems.get(position, -1);
        if (colIndex != -1) {
            return mColumns[colIndex];
        }

        // 调整位置(排除头…)
        position = Math.max(0, position - getHeaderViewsCount());

        final int lastVisiblePos = Math.max(0, position);
        if (lastVisiblePos < getColumnNumber())
            return mColumns[lastVisiblePos];

        if (flow) {
            // 找到列具有最小的底值。
            return gettBottomColumn();
        } else {
            //找到列的最小值最高。
            return getTopColumn();
        }
    }

    private boolean isHeaderOrFooterPosition(int pos) {
        int type = mAdapter.getItemViewType(pos);
        return type == ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
    }

    private Column getTopColumn() {
        Column result = mColumns[0];
        for (Column c : mColumns) {
            result = result.getTop() > c.getTop() ? c : result;
        }
        return result;
    }

    private Column gettBottomColumn() {
        Column result = mColumns[0];
        for (Column c : mColumns) {
            result = result.getBottom() > c.getBottom() ? c : result;
        }

        if (DEBUG)
            Log.d("Column", "get Shortest Bottom Column: " + result.getIndex());
        return result;
    }

    private int getColumnLeft(int pos) {
        int colIndex = mItems.get(pos, -1);

        if (colIndex == -1)
            return 0;

        return mColumns[colIndex].getColumnLeft();
    }

    private int getColumnWidth(int pos) {
        int colIndex = mItems.get(pos, -1);

        if (colIndex == -1)
            return 0;

        return mColumns[colIndex].getColumnWidth();
    }

    // /////////////////////////////////////////////////////////////
    // Inner Class.
    // /////////////////////////////////////////////////////////////

    public int getColumnNumber() {
        return mColumnNumber;
    }

    private class Column {

        private int mIndex;
        private int mColumnWidth;
        private int mColumnLeft;
        private int mSynchedTop = 0;
        private int mSynchedBottom = 0;

        // TODO 可以使用项目位置信息来识别项目? ?

        public Column(int index) {
            mIndex = index;
        }

        public int getColumnLeft() {
            return mColumnLeft;
        }

        public int getColumnWidth() {
            return mColumnWidth;
        }

        public int getIndex() {
            return mIndex;
        }

        public int getBottom() {
            // 找到最大的价值。
            int bottom = Integer.MIN_VALUE;
            int childCount = getChildCount();

            for (int index = 0; index < childCount; ++index) {
                View v = getChildAt(index);

                if (v.getLeft() != mColumnLeft && isFixedView(v) == false)
                    continue;
                bottom = bottom < v.getBottom() ? v.getBottom() : bottom;
            }

            if (bottom == Integer.MIN_VALUE)
                return mSynchedBottom; // 没有孩子对这列. .
            return bottom;
        }

        public void offsetTopAndBottom(int offset) {
            if (offset == 0)
                return;

            // 找到最大的价值。
            int childCount = getChildCount();

            for (int index = 0; index < childCount; ++index) {
                View v = getChildAt(index);

                if (v.getLeft() != mColumnLeft && isFixedView(v) == false)
                    continue;

                v.offsetTopAndBottom(offset);
            }
        }

        public int getTop() {
            // 找到最小值。
            int top = Integer.MAX_VALUE;
            int childCount = getChildCount();
            for (int index = 0; index < childCount; ++index) {
                View v = getChildAt(index);
                if (v.getLeft() != mColumnLeft && isFixedView(v) == false)
                    continue;
                top = top > v.getTop() ? v.getTop() : top;
            }

            if (top == Integer.MAX_VALUE)
                return mSynchedTop; // 　　没有孩子对这个专栏。仅返回保存同步前. .
            return top;
        }

        public void save() {
            mSynchedTop = 0;
            mSynchedBottom = getTop(); // getBottom();
        }

        public void clear() {
            mSynchedTop = 0;
            mSynchedBottom = 0;
        }
    }// end of inner class Column

    private class FixedColumn extends Column {

        public FixedColumn() {
            super(Integer.MAX_VALUE);
        }

        @Override
        public int getBottom() {
            return getScrollChildBottom();
        }

        @Override
        public int getTop() {
            return getScrollChildTop();
        }

    }// end of class

}// end of class
