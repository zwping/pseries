package com.zwping.jetpack.ac.md

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zwping.jetpack.R
import com.zwping.jetpack.databinding.ItemTestBinding
import com.zwping.jetpack.ktxs.*
import com.zwping.jetpack.manager.LiveDataBus
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * zwping @ 12/25/20
 */
class DiffUtilAc : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ly_container?.also {
            val tv = AppCompatTextView(this).apply { text = "列表差量刷新" }
            it.addView(tv)
            val rv = RecyclerView(this)
            it.addView(rv)
            val adapter = rv.builder(object : BaseBindingExtraAdapter<String, ItemTestBinding>({ inflater, parent -> ItemTestBinding.inflate(inflater, parent, false) }) {
                override fun convert2(h: VBViewHolder<ItemTestBinding>, b: String) {
                    h.vb.tvTitle.text = "$b"
                }
            }) {
                removeFocus()
                // setGridLayoutManager(3)

                it.setDiffCallback2({ od, nd -> od == nd })
                it.loadMoreModule.setOnLoadMoreListener { println("loadMore ") }
                // it.emptyLayout = FrameLayout(this)
            }

            it.addView(AppCompatTextView(this).apply { text = "数据产生差异" })
            val tv1 = AppCompatTextView(this)
            it.addView(tv1)
            var size = 0
            val diff = IDiffUtil<Int>({ od, nd -> od == nd }).setOnDataDiffListener { tv1.text = "$it 发生了变化, 次数${++size}" }

            ITimer({
                adapter.setDiffNewData(mutableListOf<String>().apply {
                    for (i in 0 until 5) {
                        add("${(0..i).random()}")
                    }
                })
                tv.text = "列表差量刷新, 次数${it.count}"

                diff.calculateDiff(mutableListOf<Int>().apply {
                    for (i in 0 until 1) {
                        add((0..1).random())
                    }
                })
            }, 0, 3000).schedule(this)
        }




        LiveDataBus.TestBus.observe(this) {
            println("${javaClass.simpleName} -- $it")
        }

        val t = ITimer({
            LiveDataBus.TestBus.data = i
            ++i
        }, 0, 1000).schedule(this, 2)
        t.cancel()

    }

    companion object {
        var i = 0
    }

}