package win.zwping.pseries.fm.review.prv

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_prv_grid.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-18 17:11:36 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class GridPRvFm : BaseFm() {

    override fun setIsLazy() = false
    override fun bindChildLayout() = R.layout.fm_prv_grid

    override fun doBusiness() {
        prv?.setAdapterSup(String, android.R.layout.simple_list_item_1) {
            it?.helper?.setText(android.R.id.text1, "${it.item}")?.setBackgroundColor(android.R.id.text1, Color.GRAY)
        }
        prv?.adapterSup?.setNewData(listOf("1", "2", "3", "4", "5", "6", "7", "8"))
    }

}