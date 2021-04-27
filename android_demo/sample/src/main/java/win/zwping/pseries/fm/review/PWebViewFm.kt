package win.zwping.pseries.fm.review

import kotlinx.android.synthetic.main.fm_p_web_view.*
import win.zwping.pseries.MainActivity
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-04-09 16:41:07 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class PWebViewFm : BaseFm() {
    override fun bindChildLayout() = R.layout.fm_p_web_view

    override fun doBusiness() {
        (ac as MainActivity).setOver()
        pwv?.builder()?.loadUrl("z.cn")
    }

}