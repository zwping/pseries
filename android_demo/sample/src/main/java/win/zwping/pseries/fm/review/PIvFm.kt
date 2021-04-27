package win.zwping.pseries.fm.review

import kotlinx.android.synthetic.main.fm_piv.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-04-11 14:46:13 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class PIvFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_piv

    override fun doBusiness() {
        piv1?.displayImage("https://pic3.zhimg.com/50/v2-1d826f2c0138980e1dbf2eb184aba622_hd.jpg")
        piv2?.setRoundRect(5)?.displayImage("https://pic3.zhimg.com/50/v2-1d826f2c0138980e1dbf2eb184aba622_hd.jpg")
        piv3?.setRoundRect(5)?.displayImage("https://pic3222.zhimg.com/50/v2-1d826f2c0138980e1dbf2eb184aba622_hd.jpg") // error
        piv4?.setRoundRect(5)?.displayImage("http://img.soogif.com/iXHb8ExhxWbLtP63Bm8LTl7OPnOaoYQN.gif_s400x0")
    }

}