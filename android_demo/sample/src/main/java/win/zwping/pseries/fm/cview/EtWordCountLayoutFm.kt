package win.zwping.pseries.fm.cview

import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-05 11:50:40 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class EtWordCountLayoutFm : BaseFm() {


    override fun setIsLazy() = false

    override fun bindChildLayout() = R.layout.cview_et_word_count_layout

    override fun doBusiness() {
        println("取消懒加载")
    }

}