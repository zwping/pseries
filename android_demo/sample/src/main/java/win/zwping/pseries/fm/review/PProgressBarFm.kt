package win.zwping.pseries.fm.review

import android.graphics.Color
import kotlinx.android.synthetic.main.fm_progress_bar.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 *
 * describe：
 *     note：
 *  @author：zwp on 2019-04-11 09:19:26 mail：1101558280@qq.com web: http://www.zwping.win
 */
class PProgressBarFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_progress_bar

    override fun doBusiness() {
        ppb1?.setCircleColor(Color.BLACK)

        ppb2?.progress = 50
        ppb2?.secondaryProgress = 10
        ppb2?.setHorizontalParams(5, Color.parseColor("#dedede"), Color.parseColor("#00f00f"), Color.BLUE)

        ppb3?.setProgressOfAnim(90, 2000)
    }

}
