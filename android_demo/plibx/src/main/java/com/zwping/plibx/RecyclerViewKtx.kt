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
 * lasttime: 2021年09月13日14:10:41
 */
class RecyclerViewKtx

/* ====================== */

open class BaseAdapterQuick<E>(val vh: (ViewGroup) -> BaseVH<E, ViewBinding>): BaseAdapter<E>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<E, ViewBinding> {
        return vh(parent)
    }
}
abstract class BaseAdapter<E> : RecyclerView.Adapter<BaseVH<E, ViewBinding>>() {

    var datas = mutableListOf<E>()
        private set(value) {
            field = value
            pag.curPage = 1
            pag.curTotal = datas.size
            datasStateCallback?.refreshState(true)
            datasStateCallback?.noMoreData(pag.hasEnd())  // 默认协议每次返回数量为指定的PageSize(总数量%每页数量!=0 == 咩有更多数据)
        }
    var pag = Pagination()  // pageSize默认20
        private set

    var datasStateCallback: DatasStataCallback? = null   // 控制RefreshLayout状态, util配置可兼容所有刷新控件
    var diffCallback : DiffCallback<E>? = null           // 初始化 才可Diff渲染数据

    fun setDataOrNull(data: MutableList<E>?, detectMoves: Boolean = true) { setData(data ?: mutableListOf(), detectMoves) }
    fun setData(data: MutableList<E>, detectMoves: Boolean = true) {
        if (null == diffCallback) {
            datas = data; notifyDataSetChanged()
            return
        }
        _od.clear(); _od.addAll(datas); _nd.clear(); _nd.addAll(data)
        DiffUtil.calculateDiff(_diffCallBack, detectMoves).dispatchUpdatesTo(this)
        datas = data
    }
    fun addDataOrNull(data: MutableList<E>?) { if (!data.isNullOrEmpty()) addData(data) }
    fun addData(data: MutableList<E>) {
        datas.addAll(data); notifyItemRangeChanged(datas.size-data.size, datas.size)
        pag.curPage += 1
        pag.curTotal = datas.size
        datasStateCallback?.noMoreData(pag.hasEnd())
        datasStateCallback?.loadMoreState(true)
        // datasStateCallback?.loadMoreEnabled(!pag.hasEnd())
    }
    fun setDataErr(hasRefresh: Boolean) {
        datasStateCallback?.apply {
            if (hasRefresh) {
                refreshState(false)
                if (datas.size == 0) noMoreData(true)
            } else loadMoreState(false)
        }
    }

    // override fun getItemViewType(position: Int): Int { } // 多布局实现
    override fun onBindViewHolder(holder: BaseVH<E, ViewBinding>, position: Int) { holder.bind(datas, position) }

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

open class BaseVH<E, out VB : ViewBinding>(val vb: VB, private val bindViewHolder: BaseVH<*, *>.(vb: VB, entity: E) -> Unit) : RecyclerView.ViewHolder(vb.root) {
    var entity: E? = null
    private lateinit var _datas: MutableList<E>
    fun bind(datas: MutableList<E>, position: Int) { _datas = datas; this.entity = datas[position]; bindViewHolder(this, vb, entity!!) }

    fun isLastPosition() = _datas.size-1 == adapterPosition
}

interface DiffCallback<E> {
    fun areItemsTheSame(od: E, nd: E): Boolean
    fun areContentsTheSame(od: E, nd: E): Boolean = false
    fun getChangePayload(od: E, nd: E): Any? = null
}
interface DatasStataCallback {
    fun refreshState(suc: Boolean)          // 刷新成功或失败
    fun loadMoreState(suc: Boolean)         // 加载更多数据成功或失败
    fun noMoreData(no: Boolean)             // 没有更多数据
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

    var curPage = 1
    var curTotal = 0

    var nextPage = -1
    var pageSize = 20
    var totalPage = -1
    var totalSize = -1

    fun nextPage(r: Boolean): Int = if (r) 1 else curPage + 1

    /*** 是否到达最后一页 ***/
    fun hasEnd(): Boolean
            = curTotal == 0 ||
            (curTotal > 0 &&
                    (
                            (nextPage > -1 && curPage >= nextPage) ||
                                    (totalPage > -1 && curPage >= totalPage) ||
                                    (totalSize > -1 && curTotal >= totalSize) ||
                                    ((-1 == nextPage && -1 == totalPage && -1 == totalSize) && curTotal % pageSize != 0)  // 未填充总页数或总数，则以每页约定数量PageSize取模判断是否最后一页
                            ))
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
