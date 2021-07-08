package com.zwping.plibx;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

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

    public static abstract class BaseAdapter<E> extends RecyclerView.Adapter<BaseVH<E, ViewBinding>> {
        protected List<E> datas = new ArrayList<>();
        public List<E> getDatas() { return datas; }

        public DiffCallback<E> diffCallback;
        public void setData(List<E> data) { setData(data, true); }
        public void setData(List<E> data, boolean detectMoves) {
            if (data == null) { return; }
            if (null == diffCallback) {
                datas = data; notifyDataSetChanged();
                return;
            }
            _od.clear(); _od.addAll(datas);
            _nd.clear(); _nd.addAll(data);
            DiffUtil.calculateDiff(_diffCallBack, detectMoves).dispatchUpdatesTo(this);
            datas = data;
        }
        public void addData(List<E> data) {
            if (null == data || data.size() == 0) { return; }
            datas.addAll(data); notifyItemRangeChanged(datas.size()-data.size(), datas.size());
        }

        @Override
        public int getItemViewType(int position) { return super.getItemViewType(position); } // 多布局实现
        @Override
        public void onBindViewHolder(@NonNull BaseVH<E, ViewBinding> holder, int position) { holder.bind(datas.get(position)); }

        @Override
        public int getItemCount() { return datas.size(); }

        private final List<E> _od = new ArrayList<>();
        private final List<E> _nd = new ArrayList<>();
        private final DiffUtil.Callback _diffCallBack = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return _od.size(); }
            @Override
            public int getNewListSize() { return _nd.size(); }

            @Override
            public boolean areItemsTheSame(int op, int np) { return diffCallback==null ? false : diffCallback.areItemsTheSame(_od.get(op), _nd.get(np)); }
            @Override
            public boolean areContentsTheSame(int op, int np) { return diffCallback==null ? false : diffCallback.areContentsTheSame(_od.get(op), _nd.get(np)); }
            @Nullable
            @Override
            public Object getChangePayload(int op, int np) { return diffCallback==null ? super.getChangePayload(op, np) : diffCallback.getChangePayload(_od.get(op), _nd.get(np)); }
        };
    }

    public static class BaseVH<E, VB extends ViewBinding> extends RecyclerView.ViewHolder {
        protected VB vb;
        protected E entity;
        private final OnBindViewHolder<E, VB> onBindViewHolder;

        public BaseVH(VB vb, OnBindViewHolder<E, VB> onBindViewHolder) {
            super(vb.getRoot());
            this.vb = vb;
            this.onBindViewHolder = onBindViewHolder;
        }

        public void bind(E e) { this.entity = e; onBindViewHolder.invoke(vb, e); }

        public Context getCtx() { return itemView.getContext(); }
    }
    public interface OnBindViewHolder<E, VB extends ViewBinding> { void invoke(VB vb, E e); }

    public static abstract class DiffCallback<E> {
        public abstract Boolean areItemsTheSame(E od, E nd);
        public Boolean areContentsTheSame(E od, E nd) { return false; }
        public Object getChangePayload(E od, E nd) { return null; }
    }

    /*** 管理分页信息 ***/
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
            try { super.onLayoutChildren(recycler, state); } catch (Exception e) {  }
        }
    }

    public static class GridLayoutManager2 extends GridLayoutManager {

        public GridLayoutManager2(Context context, int spanCount, int orientation, boolean reverseLayout) { super(context, spanCount, orientation, reverseLayout); }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try { super.onLayoutChildren(recycler, state); } catch (Exception e) {  }
        }
    }

}