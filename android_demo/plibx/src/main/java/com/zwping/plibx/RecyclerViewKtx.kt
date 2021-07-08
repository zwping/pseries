package com.zwping.plibx

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.viewbinding.ViewBinding

/**
 * ViewBinding的成熟推动了原生Adapter实用
 * zwping @ 5/10/21
 */
class RecyclerViewKtx

/* ====================== */

abstract class BaseAdapter<E> : RecyclerView.Adapter<BaseVH<E, ViewBinding>>() {

    var datas = mutableListOf<E>()
        private set

    var diffCallback : DiffCallback<E>? = null  // 重新初始化 才可友善刷新
    fun setData(data: MutableList<E>, detectMoves: Boolean = true) {
        if (null == diffCallback) {
            datas = data; notifyDataSetChanged()
            return
        }
        _od.clear(); _od.addAll(datas); _nd.clear(); _nd.addAll(data)
        DiffUtil.calculateDiff(_diffCallBack, detectMoves).dispatchUpdatesTo(this)
        datas = data

    }
    fun addData(data: MutableList<E>) { datas.addAll(data); notifyItemRangeChanged(datas.size-data.size, datas.size) }

    // override fun getItemViewType(position: Int): Int { } // 多布局实现
    override fun onBindViewHolder(holder: BaseVH<E, ViewBinding>, position: Int) { holder.bind(datas[position]) }

    override fun getItemCount(): Int = datas.size

    private val _od by lazy { mutableListOf<E>() }
    private val _nd by lazy { mutableListOf<E>() }
    private val _diffCallBack by lazy {
        object: DiffUtil.Callback(){
            override fun getOldListSize(): Int = _od.size
            override fun getNewListSize(): Int = _nd.size

            override fun areItemsTheSame(op: Int, np: Int): Boolean = diffCallback?.areItemsTheSame(_od[op], _nd[np]) ?: false
            override fun areContentsTheSame(op: Int, np: Int): Boolean = diffCallback?.areContentsTheSame(_od[op], _nd[np]) ?: false
            override fun getChangePayload(op: Int, np: Int): Any? = diffCallback?.getChangePayload(_od[op], _nd[np])
        }
    }
}

abstract class DiffCallback<E> {
    abstract fun areItemsTheSame(od: E, nd: E): Boolean
    fun areContentsTheSame(od: E, nd: E): Boolean = false
    fun getChangePayload(od: E, nd: E): Any? = null
}

open class BaseVH<E, out VB : ViewBinding>(val vb: VB, private val bindViewHolder: (vb: VB, entity: E) -> Unit) : RecyclerView.ViewHolder(vb.root) {
    var entity: E? = null
    fun bind(entity: E) { this.entity = entity; bindViewHolder.invoke(vb, entity) }
}

fun ViewGroup.getLayoutInflater(): LayoutInflater { return LayoutInflater.from(context) }

inline fun RecyclerView.removeFocus() { isFocusableInTouchMode = false; requestFocus() }
inline fun RecyclerView.setLinearLayoutManager(
                    @RecyclerView.Orientation ort: Int = RecyclerView.VERTICAL,
                    noScrollV: Boolean = false,
                    noScrollH: Boolean = false) {
    layoutManager = object : LinearLayoutManager2(context, ort, false) {
        override fun canScrollVertically(): Boolean {
            return if (noScrollV) false else super.canScrollVertically()
        }
        override fun canScrollHorizontally(): Boolean {
            return if (noScrollH) false else super.canScrollHorizontally()
        }
    }
}
inline fun RecyclerView.setGradLayoutManager(
                    spanCount: Int,
                    @RecyclerView.Orientation ort: Int = RecyclerView.VERTICAL,
                    noScrollV: Boolean = false,
                    noScrollH: Boolean = false) {
    layoutManager = object : GridLayoutManager2(context, spanCount, ort, false) {
        override fun canScrollVertically(): Boolean {
            return if (noScrollV) false else super.canScrollVertically()
        }
        override fun canScrollHorizontally(): Boolean {
            return if (noScrollH) false else super.canScrollHorizontally()
        }
    }
}

/*** 管理分页信息  */
open class Pagination {
    constructor() {}
    constructor(totalSize: Int) { this.totalSize = totalSize }

    var curPage = 1
    var curTotal = 0
    var pageSize = 20
    var totalPage = -1
    var totalSize = -1
    fun setTotalPage(totalPage: Int): Pagination {
        this.totalPage = totalPage; return this
    }

    fun nextPage(r: Boolean): Int = if (r) 1 else curPage + 1

    /**
     * 是否到达最后一页, (最新返回数据为null时不在考虑范围内, 主要计算data<=pageSize)
     * @param inPageSize 依据pageSize来计算, 返回数<pageSize></pageSize>
     *
     */
    @JvmOverloads
    fun hasEnd(inPageSize: Boolean = true): Boolean {
        return totalPage > -1 && curPage >= totalPage || totalSize > -1 && curTotal >= totalSize || inPageSize && curTotal % pageSize != 0
    }
}

// diffUtil中易出现 Fix Google Bug https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in/37050829
open class LinearLayoutManager2(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try { super.onLayoutChildren(recycler, state) }
        catch (e: Exception) { }
    }
}
open class GridLayoutManager2(context: Context?, spanCount: Int, orientation: Int, reverseLayout: Boolean) :
    GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try { super.onLayoutChildren(recycler, state) }
        catch (e: Exception) { }
    }
}