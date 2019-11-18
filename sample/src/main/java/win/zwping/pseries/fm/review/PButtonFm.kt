package win.zwping.pseries.fm.review

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_review_p_button.*
import win.zwping.code.constant.ViewState
import win.zwping.code.utils.DrawableUtil
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm


/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-12 14:45:12 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class PButtonFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_review_p_button

    override fun doBusiness() {
        pbt?.setOnClickListener { pbt?.startCountDown(this) }
        pbt1?.setOnClickListener { pbt1?.startCountDown(this) }

//        pbtn2.setSelectedBgColor(Color.BLUE).setSelectedTextColor(Color.DKGRAY).isSelected = true
//        pbtn2?.setOnClickListener { }
    }
}