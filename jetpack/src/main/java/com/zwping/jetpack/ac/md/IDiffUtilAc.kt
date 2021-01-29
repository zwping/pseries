package com.zwping.jetpack.ac.md

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.zwping.jetpack.R
import com.zwping.jetpack.ktxs.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * [com.zwping.jetpack.ktxs.IDiffUtil] 案例
 * zwping @ 1/6/21
 */
@Route(path = "/jetpack/idiffutil")
class IDiffUtilAc : AppCompatActivity(R.layout.activity_main) {

    class VH(val tv: AppCompatTextView) : RecyclerView.ViewHolder(tv)
    abstract class Adp : RecyclerView.Adapter<VH>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ly_container?.also { ly ->
            ly.addView(AppCompatTextView(this).apply { text = "监听列表data产生差异" })
            val data1 = mutableListOf(0)
            val tv1 = AppCompatTextView(this).apply { text = "$data1 原始数据" }
            ly.addView(tv1)
            var diffSize1 = 0
            val diff1 = IDiffUtil<Int>({ od, nd -> od == nd }).setOnDataDiffListener { tv1.text = "$oldData To $it 发生了变化, 变化次数${++diffSize1}" }
            diff1.calculateDiff(data1) // 第一次比对

            val tv2 = AppCompatTextView(this).apply { text = "recyclerview Adapter DiffUtil" }
            ly.addView(tv2)
            val data2 = mutableListOf(0, 1, 2, 3, 4)
            val rv = RecyclerView(this)
            ly.addView(rv)
            val adp = object : Adp() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
                    return VH(AppCompatTextView(parent.context).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 20F.dp2px());setBackgroundColor(Color.GRAY) })
                }

                override fun getItemCount(): Int = data2.size

                override fun onBindViewHolder(holder: VH, position: Int) {
                    holder.tv.text = "${data2[position]}"
                }
            }
            rv.layoutManager = LinearLayoutManager2(this, RecyclerView.VERTICAL, false)
            rv.adapter = adp
            val diff2 = IDiffUtil<Int>({ od, nd -> od == nd })

            ITimer({
                val newData1 = mutableListOf((0..3).random())
                diff1.calculateDiff(newData1) // 每次都比对
                val newData2 = mutableListOf((0..10).random(), (0..10).random(), (0..10).random(), (0..10).random(), (0..10).random())
                diff2.calculateDiff(newData2).dispatchUpdatesTo(adp)
            }, 2000, 2000).schedule(this)
        }
    }

}