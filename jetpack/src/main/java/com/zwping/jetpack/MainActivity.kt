package com.zwping.jetpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zwping.jetpack.ac.md.ToolbarAc
import kotlinx.android.synthetic.main.activity_main.*

// TODO: 2020/11/12 当前界面改造为mvvm模式
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, ToolbarAc::class.java))
        btn_toolbar?.setOnClickListener {
            startActivity(Intent(this, ToolbarAc::class.java))
        }
        btn_activity_result?.setOnClickListener { // https://mp.weixin.qq.com/s/lWayiBS4T4EHcsUIgnhJzA

        }
        btn_broadcast?.setOnClickListener {

        }
    }
}