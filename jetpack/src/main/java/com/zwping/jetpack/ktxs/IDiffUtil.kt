package com.zwping.jetpack.ktxs

import androidx.recyclerview.widget.DiffUtil

/**
 * DiffUtil封装扩展
 * - 类对象管理oldData
 * - 监听data数据是否已产生差异 (DiffUtil差量算法)
 * zwping @ 12/3/20
 *
 * public class [IDiffUtil]
 *  class method [calculateDiff] [setOnDataDiffListener]
 *
 * @param areItemsTheSame
 * @param areContentsTheSame
 * @param getChangePayload
 */
class IDiffUtil<B>(
        private val areItemsTheSame: (od: B?, nd: B?) -> Boolean,
        private val areContentsTheSame: (od: B?, nd: B?) -> Boolean = { _, _ -> true },
        private val getChangePayload: ((od: B?, nd: B?) -> Any?)? = { _, _ -> null }
) {

    var oldData: List<B>? = null // 老数据/当前数据
    private var dataDiffListener: ((List<B>?) -> Unit)? = null


    /*** 类对象管理oldData ***/
    fun calculateDiff(data: List<B>?, detectMoves: Boolean = true): DiffUtil.DiffResult {
        // println("需要论证类对象管理oldData中oldData和data深拷贝问题: $oldData $data")
        val result = Callback(oldData, data, { this.oldData = it; dataDiffListener?.invoke(it) }, areItemsTheSame, areContentsTheSame, getChangePayload)
        return DiffUtil.calculateDiff(result, detectMoves)
    }

    /**
     * 监听data数据是否已产生差异
     * 借助DiffUtil差量算法实现List高效比对
     */
    fun setOnDataDiffListener(lis: (List<B>?) -> Unit): IDiffUtil<B> {
        this.dataDiffListener = lis;return this
    }

    /* ------------------------------ */

    /**
     * DiffUtil Callback 封装
     * @param oldData 老数据
     * @param newData 新数据
     * @param dataDiffListener 数据有差异的结果监听
     * @param areItemsTheSame 新老数据Item是否相同(业务ID比对) - 粗粒度
     * @param areContentsTheSame 新老数据Item内容是否相同(关键对象比对) - 细粒度
     * @param getChangePayload 新老数据Item内容如何不同(返回值会作为payloads传入onBindViewHolder())
     */
    private class Callback<B>(
            private val oldData: List<B>?,
            private val newData: List<B>?,
            private val dataDiffListener: (newData: List<B>?) -> Unit,
            private val areItemsTheSame: (oldData: B?, newData: B?) -> Boolean?,
            private val areContentsTheSame: (oldData: B?, newData: B?) -> Boolean?,
            private val getChangePayload: ((oldData: B?, newData: B?) -> Any?)? = null) : DiffUtil.Callback() {

        private var oneResponse = true

        init {
            if (oldListSize != newListSize) { // 对象判断 - 粗粒度
                invoke()
            }
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val result = areItemsTheSame(oldData?.get(oldItemPosition), newData?.get(newItemPosition))
            if (result == false) {
                invoke()
            }
            return result ?: true
        }


        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val result = areContentsTheSame(oldData?.get(oldItemPosition), newData?.get(newItemPosition))
            if (result == false) {
                invoke()
            }
            return result ?: true
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return getChangePayload?.invoke(oldData?.get(oldItemPosition), newData?.get(newItemPosition))
        }

        override fun getOldListSize(): Int = oldData?.size ?: 0

        override fun getNewListSize(): Int = newData?.size ?: 0


        private fun invoke() {
            if (oneResponse) {
                dataDiffListener.invoke(newData)
                oneResponse = false
            }
        }
    }


}