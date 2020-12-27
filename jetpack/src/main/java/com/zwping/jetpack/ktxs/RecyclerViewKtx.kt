package com.zwping.jetpack.ktxs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * recyclerview (BaseRecyclerViewAdapterHelper) 扩展
 *
 * public method [builder] [removeFocus] [setLinearLayoutManager] [setGridLayoutManager] [setDiffCallback2]
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

/*** adapter setDiffCallback ***/
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

inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> ADP.setOnLoadMoreListener(): ADP {

    return this
}

inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> ADP.setSucData(): ADP {

    return this
}

inline fun <B, VH : BaseViewHolder, ADP : BaseQuickAdapter<B, VH>> ADP.setErrorData(): ADP {

    return this
}


/* ------------------ */


/*** 管理分页信息 ***/
class Pagination {
    fun nextPage(r: Boolean) = if (r) 1 else curPage + 1

    var curPage = 1
    var curTotal = 0
    var pageSize = 20
    var total = 0
}

class VBViewHolder<VB : ViewBinding>(val vb: VB) : BaseViewHolder(vb.root)

/*** viewBinding & extra支持 ***/
abstract class BaseBindingExtraAdapter<T, VB : ViewBinding>(val createVB: (inflater: LayoutInflater, parent: ViewGroup) -> VB) :
        BaseQuickAdapter<T, VBViewHolder<VB>>(0, null), LoadMoreModule {

    // extra params
    var pagination = Pagination()

    abstract fun convert2(h: VBViewHolder<VB>, b: T) // 无作用, (h/b)个性化输出

    override fun convert(holder: VBViewHolder<VB>, item: T) {
        convert2(holder, item)
    }

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

