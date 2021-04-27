package win.zwping.pseries.fm.review.prv

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_prv_decoration.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-20 11:10:51 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class DecorationPRvFm : BaseFm() {
    override fun setIsLazy() = false
    override fun bindChildLayout() = R.layout.fm_prv_decoration

    override fun doBusiness() {
        prv1?.setAdapterSup(String(), android.R.layout.simple_list_item_1) {
            it?.helper?.setText(android.R.id.text1, it.item)
        }
        prv2?.setAdapterSup(String(), android.R.layout.simple_list_item_1) {
            it?.helper?.setText(android.R.id.text1, it.item)
        }

        val list = listOf("1", "2", "3", "4", "5", "6", "7", "8")
        prv1?.adapterSup?.setNewData(list)
        prv2?.adapterSup?.setNewData(list)
    }

}