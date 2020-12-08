package win.zwping.pseries

import com.chad.library.adapter.base.BaseViewHolder
import org.junit.Test
import win.zwping.frame.mvvm.BaseVModel

class Test6 {
    @Test
    fun test() {
        val m = M()
        m.notifyChange {
            println(it)
        }
        m.setPostBean(Bean(1, "2"))
        m.setPostBean(Bean(1, "2"))
    }

    class M : BaseVModel<Bean>()

    class Bean(var id: Int?, var name: String?)
}