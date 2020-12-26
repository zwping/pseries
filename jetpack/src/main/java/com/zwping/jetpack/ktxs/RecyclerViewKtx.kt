package com.zwping.jetpack.ktxs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zwping.jetpack.R
import com.zwping.jetpack.databinding.ActivityMainBinding

/**
 * recyclerview 扩展
 *  - BaseRecyclerViewAdapterHelper
 *
 * zwping @ 12/24/20
 */

/*** 去除焦点 ***/
inline fun RecyclerView.removeFocus() {
    isFocusableInTouchMode = false;requestFocus()
} // 在scrollView的根布局下增加 android:descendantFocusability="blocksDescendants"

/*** 线性布局 ***/
inline fun RecyclerView.setLinearLayoutManager(@RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL, noScrollV: Boolean = false, noScrollH: Boolean = false) {
    layoutManager = object : LinearLayoutManager(context, orientation, false) {
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
    layoutManager = object : GridLayoutManager(context, spanCount, orientation, false) {
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

inline fun RecyclerView.adapterSur(): BaseQuickAdapter<*, *> = adapter as BaseQuickAdapter<*, *>

abstract class BaseBindingAdapter<T, VB : ViewBinding>(data: MutableList<T>? = null) : BaseQuickAdapter<T, BaseViewHolder>(0, data) {

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    abstract fun convert(holder: BaseViewHolder, vb: VB, item: T)

    lateinit var viewBinding: VB

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        viewBinding = createViewBinding(LayoutInflater.from(parent.context), parent)
        return createBaseViewHolder(viewBinding.root)
    }

    override fun convert(holder: BaseViewHolder, item: T) {
        convert(holder, viewBinding, item)
    }

}

inline fun RecyclerView.builder(dsl: RecyclerView.() -> Unit) {
    setLinearLayoutManager()
    var page = 1
}

class RvExtra {
    var page = 1

}