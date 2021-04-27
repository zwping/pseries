package win.zwping.pseries.fm.review.prv

import kotlinx.android.synthetic.main.fm_prv_auto_load_more_of_refresh.*
import win.zwping.code.utils.HandlerUtil
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 *
 * describe：
 *     note：
 *  @author：zwp on 2019-05-06 11:26:25 mail：1101558280@qq.com web: http://www.zwping.win
 */
class AutoLoadMoreOfRefreshPRvFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_prv_auto_load_more_of_refresh

    override fun doBusiness() {
        prv?.setAdapterSup(B(), android.R.layout.simple_list_item_1) {
            it?.helper?.setText(android.R.id.text1, "${it.item}")
        }
//        prv?.setNewData(getList())

        getData()

        refresh_layout?.setOnRefreshListener {
            //            refresh_layout.finishRefresh(false)
            getData()
        }
        refresh_layout?.setOnLoadMoreListener {
            //            prv.addData(getList()).loadMoreComplete()
            getData(false)
        }
    }

    private fun getData(refresh: Boolean = true) {
        HandlerUtil.runOnUiThreadDelay({
            when {
                refresh -> prv?.setSucDataSmartFillOfRefreshLayout(refresh, getList(), 20, refresh_layout)
                ++d % 2 == 0 -> prv?.setSmartError(false, refresh_layout)
                else ->
                    prv?.setSucDataSmartFillOfRefreshLayout(refresh, getList(), 20, refresh_layout)
            }
        }, 100)
    }

    private var d = 0

    data class B(var s: Int = 0)

    private fun getList(): List<B> {
        return arrayListOf(B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n), B(++n))
    }

    private var n = 0

}
