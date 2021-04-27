package win.zwping.pseries.fm.cview

import kotlinx.android.synthetic.main.cview_switch_page_states_layout_fm.*
import win.zwping.code.utils.HandlerUtil
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-07 10:10:13 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class SwitchPageStateLayoutFm : BaseFm() {

    override fun bindChildLayout() = R.layout.cview_switch_page_states_layout_fm

    override fun doBusiness() {
        loading_tv?.setOnClickListener { switch_page_layout?.showLoading() }
        empty_tv?.setOnClickListener { switch_page_layout?.showEmpty() }
        net_error_tv?.setOnClickListener { switch_page_layout?.showNetErrorWork() }
        error_tv?.setOnClickListener { switch_page_layout?.showError() }
        cus_view_tv?.setOnClickListener { switch_page_layout?.showCusView() }
        content_tv?.setOnClickListener { switch_page_layout?.showContent() }

        switch_page_layout?.setOnRetryClickListener { switch_page_layout?.showLoading();showToast("刷新，重新获取数据") }

        showToast("默认加载Loading...")
        HandlerUtil.runOnUiThreadDelay({ if (isChangeUi) switch_page_layout?.showContent() }, 1000)
    }

}