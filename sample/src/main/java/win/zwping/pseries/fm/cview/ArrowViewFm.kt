package win.zwping.pseries.fm.cview

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_cview_more_view.*
import win.zwping.code.constant.Direction
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
        arrow_view?.setArrowColor(Color.YELLOW)?.setArrowDirection(Direction.Down)?.setArrowWidth(2)?.init()
    }

}