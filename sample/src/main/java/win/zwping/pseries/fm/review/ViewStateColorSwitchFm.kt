package win.zwping.pseries.fm.review

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_view_state_color_switch.*
import win.zwping.code.comm.ViewStateColor
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-04-04 16:02:55 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class ViewStateColorSwitchFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_view_state_color_switch

    override fun doBusiness() {
        ptv01?.setOnClickListener { it.isSelected = !it.isSelected }
        ptv02?.setOnClickListener { it.isEnabled = !it.isEnabled }
        ptv03?.setOnClickListener { }
        ptv04?.setOnClickListener { }
        ptv05?.setOnClickListener { }
        ptv06?.setOnClickListener { }

        val bc = Color.parseColor("#0ff0ff")
        val tc = Color.WHITE

        ptv11?.setVStateBgColor(ViewStateColor().setSelected(bc))
                ?.setVStateTextColor(ViewStateColor().setSelected(tc))
                ?.setOnClickListener { it.isSelected = !it.isSelected }
        ptv12?.setVStateBgColor(ViewStateColor().setUnable(bc).setNormal(Color.BLUE))
                ?.setVStateTextColor(ViewStateColor().setUnable(tc))
                ?.setOnClickListener { it.isEnabled = !it.isEnabled }
        ptv13?.setVStateBgColor(ViewStateColor().setPressed(bc))
                ?.setVStateTextColor(ViewStateColor().setPressed(tc))
                ?.setOnClickListener { }
        // ptv14?.setVStateBgColor(ViewStateColor())?.setVStateTextColor(ViewStateColor())?.setOnClickListener { }
        ptv15?.setVStateBgColor(ViewStateColor().setFocused(bc))
                ?.setVStateTextColor(ViewStateColor().setFocused(tc))
                ?.setOnClickListener { }
        ptv16?.setVStateBgColor(ViewStateColor())
                ?.setVStateTextColor(ViewStateColor())
                ?.setOnClickListener { }
    }

}