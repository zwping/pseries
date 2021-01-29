package com.zwping.jetpack

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zwping.jetpack.ktxs.setOnDebounceClickListener
import kotlinx.android.synthetic.main.activity_main.*

// TODO: 2020/11/12 当前界面改造为mvvm模式
@Route(path = "/jetpack/main")
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startActivity(Intent(this, ViewModelAc::class.java).apply { putExtra("ids", "value1") })
//        startA(DiffUtilAc::class.java)
        // start("/jetpack/retrofit")
        ly_container?.also {
            it.addView(cb().apply { text = "Toolbar";setOnDebounceClickListener { start("/jetpack/toolbar") } })
            // @Deprecated("常见模式中回执需要在onResume中接收, 可直接以viewModel形式替代")
            // it.addView(cb().apply { text = "Activity Result";setOnDebounceClickListener { } }) // https://mp.weixin.qq.com/s/lWayiBS4T4EHcsUIgnhJzA
            it.addView(cb().apply { text = "IDiffUtilCall";setOnDebounceClickListener { start("/jetpack/idiffutil") } })
            it.addView(cb().apply { text = "Java2XmlSelectedAc";setOnDebounceClickListener { start("/jetpack/java2xmlselected") } })
            // it.addView(cb().apply { text = "Broadcast Receiver";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "ARouter";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Retrofit";setOnDebounceClickListener { start("/jetpack/retrofit") } })
            it.addView(cb().apply { text = "ViewPager2";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "ViewModel";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "DataStore";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Room";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "Paging3";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Fragment";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Coroutines";setOnDebounceClickListener { } })
        }
    }

    private fun cb(): AppCompatButton = AppCompatButton(this).apply { isAllCaps = false; gravity = Gravity.START or Gravity.CENTER_VERTICAL }

    private fun start(path:String){
        ARouter.getInstance().build(path).navigation()
    }
}