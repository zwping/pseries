package win.zwping.pseries.fm.review

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import kotlinx.android.synthetic.main.fm_card_view.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm
import android.graphics.drawable.StateListDrawable
import win.zwping.code.constant.ViewState


/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-04-01 14:09:01 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class
PCardViewFm : BaseFm() {
    override fun bindChildLayout() = R.layout.fm_card_view

    override fun doBusiness() {
        cv1?.isEnabled = false
        cv2.setOnClickListener {
            it.isSelected = !it.isSelected
            showToast("selected: ${it.isSelected}")
        }
    }

}