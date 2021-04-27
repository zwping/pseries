package com.zwping.jetpack.ac.md

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zwping.jetpack.R
import com.zwping.jetpack.databinding.ItemTestBinding
import com.zwping.jetpack.ktxs.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * zwping @ 12/25/20
 */
@Route(path = "/jetpack/diffutil")
class DiffUtilAc : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ly_container?.also { it ->
            it.addView(AppCompatTextView(this).apply { text = "数据产生差异" })
            val tv1 = AppCompatTextView(this)
            it.addView(tv1)
            var size = 0
            val diff = IDiffUtil<Int>({ od, nd -> od == nd }).setOnDataDiffListener { tv1.text = "$it 发生了变化, 次数${++size}" }


            val tv = AppCompatTextView(this).apply { text = "列表差量刷新" }
            it.addView(tv)
            val refreshLayout = SmartRefreshLayout(this).apply {
            }
            refreshLayout.setRefreshHeader(ClassicsHeader(this))
            refreshLayout.setRefreshFooter(ClassicsFooter(this))
            val rv = RecyclerView(this).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 600)
                setBackgroundColor(Color.parseColor("#666666"))
            }
            refreshLayout.addView(rv)
            it.addView(refreshLayout)

            val adapter = rv.builder(object : BaseBindingExtraAdapter<String, ItemTestBinding>({ inflater, parent -> ItemTestBinding.inflate(inflater, parent, false) }) {
                override fun convert(holder: VBViewHolder<ItemTestBinding>, item: String) {
                    holder.vb.tvTitle.text = "$item"
                }
            }) {
                removeFocus()
                // setGridLayoutManager(3)

                it.pagination.pageSize = 5
                it.setDiffCallback2({ od, nd -> od == nd })
                // it.emptyLayout = FrameLayout(this)
            }


            val pag = Pagination(3)
//            refreshLayout.setOnRefreshListener {
//                mockData(adapter.pagination.nextPage(true), adapter.pagination.pageSize,
//                        {
//                            adapter.setDataSuc(it, true, refreshLayout, pag)
//                        },
//                        {
//                            adapter.setDataErr(true, refreshLayout)
//                        })
//            }
//            refreshLayout.setOnLoadMoreListener {
//                mockData(adapter.pagination.nextPage(false), adapter.pagination.pageSize,
//                        {
//                            adapter.setDataSuc(it, false, refreshLayout, pag)
//                        },
//                        {
//                            adapter.setDataErr(false, refreshLayout)
//                        })
//            }
//
//            adapter.loadMoreModule.setOnLoadMoreListener {
//                mockData(adapter.pagination.nextPage(false), adapter.pagination.pageSize,
//                        {
//                            adapter.setDataSuc(it, false, refreshLayout, pag)
//                        },
//                        { adapter.setDataErr(false, refreshLayout) })
//            }

            refreshLayout.autoRefresh(500)

            ITimer({
                adapter.setDataSuc(mutableListOf<String>().apply {
                    for (i in 0 until 5) {
                        add("${(0..i).random()}")
                    }
                }, true, refreshLayout)
                tv.text = "列表差量刷新, 次数${it.count}"

                diff.calculateDiff(mutableListOf<Int>().apply {
                    for (i in 0 until 1) {
                        add((0..1).random())
                    }
                })
//                it.cancel()
            }, 0, 3000)
             .schedule(this)
        }

    }

    companion object {
        var i = 0
    }


    private fun mockData(pageIndex: Int, pageSize: Int, lis: (MutableList<String>) -> Unit, lisErr: () -> Unit) {
        ITimer({
            var data = mutableListOf<String>().apply {
                for (i1 in (pageIndex - 1) * pageSize until pageSize * pageIndex) add("$i1")
            }
//             if (pageIndex != 1) data.removeAt(0)
//             if (pageIndex != 1) data.clear()
//            if (pageIndex == 1) for (i in 0 until 5) data.add("$i")
            lis.invoke(data)
//            lisErr.invoke()
//            if (pageIndex == 1) lis.invoke(data)
//             else lisErr.invoke()
        }, 1000).schedule(this)
    }
}