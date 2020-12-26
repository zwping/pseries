package com.zwping.jetpack.ac.md

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
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
            it.addView(AppCompatTextView(this).apply { text = "列表差量刷新" })

            val rv = RecyclerView(this).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
            it.addView(rv)
            val adapter = object : BaseBindingAdapter<String, ItemTestBinding>() {
                override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemTestBinding {
                    return ItemTestBinding.inflate(inflater, parent, false)
                }

                override fun convert(holder: BaseViewHolder, vb: ItemTestBinding, item: String) {
                    // holder.setText(R.id.tv_title,item)
                    vb.tvTitle.text = "$item"
                }
            }
            rv.setLinearLayoutManager()
            rv.adapter = adapter

            ITimer({
//                runOnUiThread {
                    adapter.setNewInstance(mutableListOf<String>().apply {
                        for (i in 0 until 10) {
                            add("${(0..i).random()}")
                        }
                    })
//                }
                println(adapter.data)
            }, 0, 2000).schedule(this)
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