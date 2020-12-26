package com.zwping.jetpack

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.ac_view_model.*

/**
 *
 * zwping @ 12/6/20
 */
class ViewModelAc : AppCompatActivity() {

    class Bean(var id: Int?, var name: String?)

    class VM1 : ViewModel() {
        var list = liveData<List<Bean>> {  }
    }

    class VM : ViewModel() {
        var id = MutableLiveData<String>()
    }

    private val vm by lazy { ViewModelProvider(this).get(VM::class.java) }
    private val vm1 by lazy { ViewModelProvider(this).get(VM1::class.java) }
    private var ids = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_view_model)

        vm.id?.value = "123"
        ids = intent?.getStringExtra("ids") ?: "000"

        tv?.text = "${vm.id.value} -- $ids"

        vm.id?.observe(this, Observer<String> {
            tv?.text = "${vm.id.value}"
        })


        vm1.list?.observe(this, Observer {
            println(it)
        })

//        Handler().postDelayed({ vm1.list.setValue(mutableListOf(Bean(1, "1"), Bean(2, "2"))) }, 1000)
//        Handler().postDelayed({ vm1.list.setValue(mutableListOf(Bean(1, "1"), Bean(2, "2"))) }, 2000)



        Handler().postDelayed({ vm.id?.value = System.currentTimeMillis().toString() }, 1000)
//        Handler().postDelayed({ vm.id = System.currentTimeMillis().toString() }, 1000)
//        Handler().postDelayed({ vm.id = System.currentTimeMillis().toString() }, 1000)
    }


}