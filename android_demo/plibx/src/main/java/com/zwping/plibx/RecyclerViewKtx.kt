package com.zwping.plibx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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