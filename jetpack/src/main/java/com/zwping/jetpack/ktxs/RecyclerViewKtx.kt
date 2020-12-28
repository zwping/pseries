package com.zwping.jetpack.ktxs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * recyclerview (BaseRecyclerViewAdapterHelper + SmartRefreshLayout) 扩展
 *
 * public method [builder] [removeFocus] [setLinearLayoutManager] [setGridLayoutManager]
 * adapter public method [setDiffCallback2]
 * public class [Pagination] [BaseBindingExtraAdapter]... [LinearLayoutManager2] [GridLayoutManager2]
 * zwping @ 12/24/20
 *
 * --------------------------------------
 *
 * RecyclerViewKtx扩展文件核心入口 [builder]
 *
 * @param dsl dsl风格完善rv配置
 * @return adp [BaseQuickAdapter]
 */
inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> RecyclerView.builder(adp: ADP, dsl: RecyclerView.(ADP) -> Unit): ADP {
    // removeFocus()
    setLinearLayoutManager() // 默认配置
    dsl.invoke(this, adp) // dsl风格配置
    adapter = adp
    return adp
}

/*** adapter setDiffCallback simple style ***/
inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> ADP.setDiffCallback2(
        crossinline areItemsTheSame2: (od: B?, nd: B?) -> Boolean, // 粗粒度
        crossinline areContentsTheSame2: (od: B?, nd: B?) -> Boolean = { _, _ -> true },
        crossinline getChangePayload2: (od: B?, nd: B?) -> Any? = { _, _ -> null }
): ADP {
    setDiffCallback(object : DiffUtil.ItemCallback<B>() {
        override fun areItemsTheSame(oldItem: B, newItem: B): Boolean = areItemsTheSame2(oldItem, newItem)

        override fun areContentsTheSame(oldItem: B, newItem: B): Boolean = areContentsTheSame2(oldItem, newItem)

        override fun getChangePayload(oldItem: B, newItem: B): Any? = getChangePayload2(oldItem, newItem)
    })
    return this
}

/**
 * 设置成功数据
 *  - 管理refresh & loadMore 状态
 *  - 管理noMoreDataTxt
 */
inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> ADP.setDataSuc(
        datas: MutableList<B>?, hasRefresh: Boolean, srl: SmartRefreshLayout?, pag: Pagination? = null, showNoMoreDataTxt: Boolean = true): ADP {
    val hasSRLLoadMore = !loadMoreModule.isEnableLoadMore // 只需设置loadMoreModule.setOnLoadMoreListener, 其就会自动管理srl & loadMoreModule
    if (!hasSRLLoadMore) srl?.setEnableLoadMore(false) // 如果用了adapterLoadMoreModule, 就主动让srlLoadMore失效
    val dataNoNull = datas != null && datas.size != 0
    if (hasRefresh) {
        setDiffNewData(datas)
        srl?.finishRefresh(true)
    } else {
        datas?.also { addData(it) }
        srl?.finishLoadMore(true)
    }
    when (this) { // 状态管理
        is BaseProviderMultiExtraAdapter<*> -> {
            pagination.stateManage(dataNoNull, this, hasRefresh, srl, pag, showNoMoreDataTxt)
        }
        is BaseBindingExtraAdapter<*, *> -> {
            pagination.stateManage(dataNoNull, this, hasRefresh, srl, pag, showNoMoreDataTxt)
        }
        else -> {
            srl?.setNoMoreData(!dataNoNull)
            if (dataNoNull) { // 有数据
                loadMoreModule.loadMoreComplete()
                if (hasRefresh && hasSRLLoadMore) srl?.setEnableLoadMore(true)
            } else { // 没有数据
                if (hasRefresh) {
                    srl?.setEnableLoadMore(false); loadMoreModule.loadMoreEnd(true)
                } else {
                    loadMoreModule.loadMoreEnd(!showNoMoreDataTxt)
                }
            }
        }
    }
    return this
}

inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> ADP.setDataErr(hasRefresh: Boolean, srl: SmartRefreshLayout?): ADP {
    when (hasRefresh) {
        true -> {
            srl?.finishRefresh(false)
            srl?.setEnableLoadMore(false)
            loadMoreModule.loadMoreEnd(true)
        }
        false -> {
            srl?.finishLoadMore(false)
            loadMoreModule.loadMoreFail()
        }
    }
    loadMoreModule.loadMoreFail()
    return this
}

/*** 管理SmartRefreshLayout & LoadMoreModule 状态 & pagination 数据 ***/
fun Pagination.stateManage(dataNoNull: Boolean, adp: BaseQuickAdapter<*, *>, hasRefresh: Boolean, srl: SmartRefreshLayout?, pag: Pagination?, showNoMoreDataTxt: Boolean) {
    totalPage = pag?.totalPage ?: 0
    totalSize = pag?.totalSize ?: 0
    curTotal = adp.data.size
    srl?.setNoMoreData(!dataNoNull)
    if (dataNoNull) { // 有数据
        curPage = if (hasRefresh) 1 else curPage + 1
        if (hasEnd()) { // 到了最后一页
            srl?.setNoMoreData(true)
            adp.loadMoreModule.loadMoreEnd(!showNoMoreDataTxt)
        } else { // 未到最后一页
            adp.loadMoreModule.loadMoreComplete()
            if (hasRefresh && !adp.loadMoreModule.isEnableLoadMore) srl?.setEnableLoadMore(true)
        }
    } else { // 没有数据
        if (hasRefresh) {
            srl?.setEnableLoadMore(false); adp.loadMoreModule.loadMoreEnd(true)
        } else {
            adp.loadMoreModule.loadMoreEnd(!showNoMoreDataTxt)
        }
    }
}

/* ------------------ */


/**
 * 管理分页信息
 * @param totalPage
 * @param totalSize 后台应返回总页数 或总数, 来有效控制是否到最后一页
 */
class Pagination(var totalPage: Int = 0, var totalSize: Int = 0) {

    fun nextPage(r: Boolean) = if (r) 1 else curPage + 1

    /**
     * 是否到达最后一页, (最新返回数据为null时不在考虑范围内, 主要计算数据</=pageSize)
     * @param inPageSize 依据pageSize来计算, 返回数<pageSize == 最后一页
     */
    fun hasEnd(inPageSize: Boolean = true): Boolean = (totalPage != 0 && curPage >= totalPage) || (totalSize != 0 && curTotal >= totalSize) || (inPageSize && curTotal % pageSize != 0)

    var curPage = 1
    var curTotal = 0
    var pageSize = 20
}

class VBViewHolder<VB : ViewBinding>(val vb: VB) : BaseViewHolder(vb.root)

/*** viewBinding & extra支持 ***/
abstract class BaseBindingExtraAdapter<T, VB : ViewBinding>(val createVB: (inflater: LayoutInflater, parent: ViewGroup) -> VB) : BaseQuickAdapter<T, VBViewHolder<VB>>(0, null), LoadMoreModule {

    // extra params...
    var pagination = Pagination()

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        return VBViewHolder(createVB(LayoutInflater.from(parent.context), parent))
    }
}

abstract class BaseProviderMultiExtraAdapter<T> : BaseProviderMultiAdapter<T>(null), LoadMoreModule {

    // extra params...
    var pagination = Pagination()
}

/* ------------------ */

/*** 去除焦点 ***/
inline fun RecyclerView.removeFocus() {
    isFocusableInTouchMode = false;requestFocus()
} // 在scrollView的根布局下增加 android:descendantFocusability="blocksDescendants"

/*** 线性布局 ***/
inline fun RecyclerView.setLinearLayoutManager(@RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL, noScrollV: Boolean = false, noScrollH: Boolean = false) {
    layoutManager = object : LinearLayoutManager2(context, orientation, false) {
        override fun canScrollVertically(): Boolean {
            if (noScrollV) return false
            return super.canScrollVertically()
        }

        override fun canScrollHorizontally(): Boolean {
            if (noScrollH) return false
            return super.canScrollHorizontally()
        }
    }
}

/*** 网格布局 ***/
inline fun RecyclerView.setGridLayoutManager(spanCount: Int = 2, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL, noScrollV: Boolean = false, noScrollH: Boolean = false) {
    layoutManager = object : GridLayoutManager2(context, spanCount, orientation, false) {
        override fun canScrollVertically(): Boolean {
            if (noScrollV) return false
            return super.canScrollVertically()
        }

        override fun canScrollHorizontally(): Boolean {
            if (noScrollH) return false
            return super.canScrollHorizontally()
        }
    }
}

/* ------------------ */

// diffUtil中易出现 Fix Google Bug https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in/37050829
open class LinearLayoutManager2(context: Context?, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

open class GridLayoutManager2(context: Context?, spanCount: Int, orientation: Int, reverseLayout: Boolean) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

