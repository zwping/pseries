package win.zwping.pseries.fm.cview.review

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import win.zwping.code.cview.MenuBar

/**
 *
 * describe：
 *     note：
 *  @author：zwp on 2019-03-27 16:34:22 mail：1101558280@qq.com web: http://www.zwping.win
 */
class MBar : MenuBar {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setTitleMarginLeft(12)
        setArrowColor(Color.BLACK)
        setArrowWH(12)
        setTitleSize(12)
    }
}
