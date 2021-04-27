package win.zwping.pseries.fm.review.vp

import android.widget.TextView
import win.zwping.pseries.base.BaseFm

/**
 *
 * describe：
 *     note：
 *  @author：zwp on 2019-03-22 15:13:20 mail：1101558280@qq.com web: http://www.zwping.win
 */
class PItemViewPagerFm : BaseFm() {

    private var position = 0

    fun setP(p: Int): PItemViewPagerFm {
        this.position = p
        return this
    }

    override fun bindChildLayout() = android.R.layout.simple_list_item_1

    override fun doBusiness() {
        findViewById<TextView>(android.R.id.text1)?.text = "$position"
    }

    override fun setIsLazy() = false

    override fun hideFActionButton() = true


}
