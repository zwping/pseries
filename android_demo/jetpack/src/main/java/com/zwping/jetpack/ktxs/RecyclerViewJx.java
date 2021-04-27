package com.zwping.jetpack.ktxs;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * zwping @ 4/23/21
 */
public class RecyclerViewJx {


    public static RecyclerView removeFocus(RecyclerView rv) { rv.setFocusableInTouchMode(false); rv.requestFocus(); return rv; }
    // 在scrollView的根布局下增加 android:descendantFocusability="blocksDescendants"

    public static RecyclerView setLinearLayoutManager(RecyclerView rv) { return setLinearLayoutManager(rv, false, false); }
    public static RecyclerView setLinearLayoutManager(RecyclerView rv, boolean noScrollV, boolean noScrollH) { return setLinearLayoutManager(rv, RecyclerView.VERTICAL, noScrollV, noScrollH); }
    public static RecyclerView setLinearLayoutManager(RecyclerView rv, @RecyclerView.Orientation int ort, boolean noScrollV, boolean noScrollH) {
        rv.setLayoutManager(new LinearLayoutManager2(rv.getContext(), ort, false){
            @Override
            public boolean canScrollVertically() {
                if (noScrollV) { return false; }
                return super.canScrollVertically();
            }
            @Override
            public boolean canScrollHorizontally() {
                if (noScrollH) { return false; }
                return super.canScrollHorizontally();
            }
        });
        return rv;
    }

    public static RecyclerView setGradLayoutManager(RecyclerView rv, int spanCount) { return setGradLayoutManager(rv, spanCount, false, false); }
    public static RecyclerView setGradLayoutManager(RecyclerView rv, int spanCount, boolean noScrollV, boolean noScrollH) { return setGradLayoutManager(rv, spanCount, RecyclerView.VERTICAL, false, false); }
    public static RecyclerView setGradLayoutManager(RecyclerView rv, int spanCount, @RecyclerView.Orientation int ort, boolean noScrollV, boolean noScrollH) {
        rv.setLayoutManager(new GridLayoutManager2(rv.getContext(), spanCount, ort, false) {
            @Override
            public boolean canScrollVertically() {
                if (noScrollV) { return false; }
                return super.canScrollVertically();
            }
            @Override
            public boolean canScrollHorizontally() {
                if (noScrollH) { return false; }
                return super.canScrollHorizontally();
            }
        });
        return rv;
    }

    /* ======================= */

    /**
     * 管理分页信息
     */
    public static class Pagination {

        public Pagination() { }
        public Pagination(int totalSize) { this.totalSize = totalSize; }

        public int curPage = 1;
        public int curTotal = 0;
        public int pageSize = 20;
        public int totalPage = -1;
        public int totalSize = -1;

        public Pagination setTotalPage(int totalPage) { this.totalPage = totalPage; return this;  }

        public int nextPage(boolean r) { return r ? 1 : curPage+1; }

        public boolean hasEnd() { return hasEnd(true); }

        /**
         * 是否到达最后一页, (最新返回数据为null时不在考虑范围内, 主要计算data<=pageSize)
         * @param inPageSize 依据pageSize来计算, 返回数<pageSize == 最后一页
         * @return
         */
        public boolean hasEnd(boolean inPageSize) {
            return (totalPage > -1 && curPage >= totalPage) || (totalSize > -1 && curTotal >= totalSize) || (inPageSize && curTotal%pageSize != 0);
        }
    }

    // diffUtil中易出现 Fix Google Bug https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in/37050829
    public static class LinearLayoutManager2 extends LinearLayoutManager {

        public LinearLayoutManager2(Context context, int orientation, boolean reverseLayout) { super(context, orientation, reverseLayout); }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try { super.onLayoutChildren(recycler, state); }
            catch (Exception e) {  }
        }
    }

    public static class GridLayoutManager2 extends GridLayoutManager{

        public GridLayoutManager2(Context context, int spanCount, int orientation, boolean reverseLayout) { super(context, spanCount, orientation, reverseLayout); }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try { super.onLayoutChildren(recycler, state); }
            catch (Exception e) {  }
        }
    }

}
