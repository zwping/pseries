package win.zwping.pseries.fm.cview

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.cview_state_layout_fm.*
import kotlinx.android.synthetic.main.fm_base.*
import win.zwping.code.cview.StateLayout
import win.zwping.code.utils.HandlerUtil
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-07 10:10:13 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class StateLayoutFm : BaseFm() {

    override fun bindChildLayout() = R.layout.cview_state_layout_fm

    override fun doBusiness() {
        state_layout.init({
            state_layout.showLoadingView()
        }, loadingLayoutId = R.layout.comm_pop_loading).viewAdjust { loadingView, emptyView, errorView ->
            emptyView?.findViewById<TextView>(R.id.tv_state_empty_txt)?.text = "可以在viewHandler中高度定制修改"
        }
        loading_tv?.setOnClickListener { state_layout?.showLoadingView() }
        empty_tv?.setOnClickListener { state_layout?.showEmptyView() }
        error_tv?.setOnClickListener { state_layout?.showErrorView("也可在方法中简便修改一下") }
        content_tv?.setOnClickListener { state_layout?.showContentView() }

//        state_layout?.setOnRetryClickListener { state_layout?.showLoading();showToast("刷新，重新获取数据") }

        HandlerUtil.runOnUiThreadDelay({ if (isChangeUi) state_layout?.showContentView() }, 1000)

        StateLayout.wrap(root_layout?.getChildAt(1)).apply {
            init { }
            HandlerUtil.runOnUiThreadDelay({ showContentView() }, 500)
        }
    }

}