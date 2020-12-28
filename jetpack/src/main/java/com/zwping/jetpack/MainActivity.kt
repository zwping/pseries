package com.zwping.jetpack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.zwping.jetpack.ac.md.DiffUtilAc
import com.zwping.jetpack.ac.md.ToolbarAc
import com.zwping.jetpack.ktxs.setOnDebounceClickListener
import com.zwping.jetpack.manager.LiveDataBus
import kotlinx.android.synthetic.main.activity_main.*

// TODO: 2020/11/12 当前界面改造为mvvm模式
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startActivity(Intent(this, ViewModelAc::class.java).apply { putExtra("ids", "value1") })
        startA(DiffUtilAc::class.java)

        LiveDataBus.TestBus.observe(this) {
            println("${javaClass.simpleName} -- $it")
        }

        ly_container?.also {
            it.addView(cb().apply { text = "Toolbar";setOnDebounceClickListener { startA(ToolbarAc::class.java) } })
            // @Deprecated("常见模式中回执需要在onResume中接收, 可直接以viewModel形式替代")
            // it.addView(cb().apply { text = "Activity Result";setOnDebounceClickListener { } }) // https://mp.weixin.qq.com/s/lWayiBS4T4EHcsUIgnhJzA
            it.addView(cb().apply { text = "DiffUtilCall";setOnDebounceClickListener { startA(DiffUtilAc::class.java) } })
            // it.addView(cb().apply { text = "Broadcast Receiver";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "ARouter";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "ViewPager2";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "ViewModel";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Retrofit";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Store";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Room";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "Paging3";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Fragment";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Coroutines";setOnDebounceClickListener { } })
        }
    }

    private fun cb(): AppCompatButton = AppCompatButton(this).apply { isAllCaps = false; gravity = Gravity.START or Gravity.CENTER_VERTICAL }

    private fun Context.startA(cls: Class<*>, lis: (Intent) -> Unit = { _ -> }) {
        startActivity(Intent(this, cls).also { lis.invoke(it) })
    }
}