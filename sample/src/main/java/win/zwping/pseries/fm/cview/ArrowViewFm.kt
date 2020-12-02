package win.zwping.pseries.fm.cview

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_cview_more_view.*
import win.zwping.code.constant.Direction
import win.zwping.code.cview.StateLayout
import win.zwping.code.utils.HandlerUtil
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-25 11:48:02 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class ArrowViewFm : BaseFm() {
    override fun bindChildLayout() = R.layout.fm_cview_more_view

    override fun doBusiness() {
        StateLayout.wrap(arrow_view).apply { init {  };HandlerUtil.runOnUiThreadDelay({showContentView()},500) }
        arrow_view?.setArrowColor(Color.YELLOW)?.setArrowDirection(Direction.Down)?.setArrowWidth(2)?.init()

    }

}