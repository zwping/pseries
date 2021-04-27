package win.zwping.pseries.fm.review.prv

import kotlinx.android.synthetic.main.fm_prv_auto_load_more.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-21 15:53:56 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class AutoLoadMorePRvFm : BaseFm() {

    override fun setIsLazy() = false

    override fun bindChildLayout() = R.layout.fm_prv_auto_load_more

    override fun doBusiness() {
        prv?.setAdapterSup(B(), android.R.layout.simple_list_item_1) {
            it?.helper?.setText(android.R.id.text1, "${it.item}")
        }
        prv?.setNewData(getList())
        prv.setOnLoadMoreListener {
            //            prv.addData(getList()).loadMoreComplete()
            prv.setSmartError(false, null)
        }

    }

    data class B(var s: Int = 0)

    private fun getList(): List<B> {
        return arrayListOf(B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n))
    }

    private var n = 0

}