package com.zwping.jetpack.ac.md

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.zwping.jetpack.R
import com.zwping.jetpack.ktxs.ColorStateList2
import com.zwping.jetpack.ktxs.create
import com.zwping.jetpack.ktxs.dp2px
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * zwping @ 1/6/21
 */
@Route(path = "/jetpack/java2xmlselected")
class Java2XmlSelectedAc : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ly_container?.also { ly ->
            ly.addView(AppCompatTextView(this).apply { text = "GradientDrawable 创建不同形状的背景" })
            ly.addView(View(this).apply {
                layoutParams = ViewGroup.LayoutParams(100F.dp2px(), 100F.dp2px())
                background = GradientDrawable().create(GradientDrawable.RECTANGLE) {
                    color = ColorStateList.valueOf(Color.GREEN)
                    setStroke(2F.dp2px(), ColorStateList.valueOf(Color.RED))
                    cornerRadii = floatArrayOf(0F, 0F, 10F.dp2px().toFloat(), 10F.dp2px().toFloat(), 0F, 0F, 20F.dp2px().toFloat(), 20F.dp2px().toFloat())
                }
            })
            ly.addView(AppCompatTextView(this).apply { text = "StateListDrawable 创建view不同状态的Drawable (点击下view查看效果)" })
            ly.addView(View(this).apply {
                layoutParams = ViewGroup.LayoutParams(100F.dp2px(), 100F.dp2px())
                val n = GradientDrawable().create(GradientDrawable.RECTANGLE) {
                    color = ColorStateList.valueOf(Color.RED)
                    setStroke(2F.dp2px(), ColorStateList.valueOf(Color.GREEN))
                    cornerRadii = floatArrayOf(0F, 0F, 10F.dp2px().toFloat(), 10F.dp2px().toFloat(), 0F, 0F, 20F.dp2px().toFloat(), 20F.dp2px().toFloat())
                }
                val s = GradientDrawable().create(GradientDrawable.RECTANGLE) {
                    color = ColorStateList.valueOf(Color.GREEN)
                    setStroke(2F.dp2px(), ColorStateList.valueOf(Color.RED))
                    cornerRadii = floatArrayOf(0F, 0F, 10F.dp2px().toFloat(), 10F.dp2px().toFloat(), 0F, 0F, 20F.dp2px().toFloat(), 20F.dp2px().toFloat())
                }
                background = StateListDrawable().create(this@Java2XmlSelectedAc, n) { pressed(s) }
                setOnClickListener { }
            })
            ly.addView(AppCompatTextView(this).apply {
                text = "ColorStateList2 创建view不同状态[States]的Color (点击查看效果)"
                setTextColor(ColorStateList2(Color.BLACK){pressed(Color.RED)}.create())
                setOnClickListener {  }
            })
        }
    }
}