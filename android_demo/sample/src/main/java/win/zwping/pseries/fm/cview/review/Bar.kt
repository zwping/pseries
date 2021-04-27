package win.zwping.pseries.fm.cview.review

import android.content.Context
import android.util.AttributeSet
import win.zwping.code.cview.Bar
import win.zwping.pseries.R

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-26 09:11:27 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class Bar : Bar {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setReturnArrowVisibility(false);setReturnPivResId(R.drawable.cus_return_icon)
    }


}