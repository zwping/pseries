package com.zwping.jetpack.ktxs

import androidx.recyclerview.widget.ListUpdateCallback
import org.junit.Test

/**
 *
 * zwping @ 12/8/20
 */
class DiffUtil {

    @Test
    fun classObjectMgDiffUtil() {
        val d = IDiffUtil<Bean>(
                { oldData, newData ->
                    oldData?.id == newData?.id
                },
                { oldData, newData ->
                    oldData?.n == newData?.n
                })
        d.setOnDataDiffListener {
            println("有改变, 新数据: $it")
        }
        // d.calculateDiff(mutableListOf(Bean(1,"1")),true).dispatchUpdatesTo(adapter)
        d.calculateDiff(mutableListOf(Bean(1, "1"))).dispatchUpdatesTo(lis)
        d.calculateDiff(mutableListOf(Bean(1, "1"))).dispatchUpdatesTo(lis)
        d.calculateDiff(mutableListOf(Bean(0, "0"), Bean(1, "1"))).dispatchUpdatesTo(lis)
    }

    val lis = object : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            println("change $position $count $payload")
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            println("moved $fromPosition $toPosition")
        }

        override fun onInserted(position: Int, count: Int) {
            println("in $position $count")
        }

        override fun onRemoved(position: Int, count: Int) {
            println("remove $position $count")
        }

    }

    data class Bean(var id: Int, var n: String?)

}
