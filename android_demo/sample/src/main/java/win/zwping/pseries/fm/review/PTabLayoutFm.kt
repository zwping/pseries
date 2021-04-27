package win.zwping.pseries.fm.review

import android.graphics.Color
import android.widget.TextView
import kotlinx.android.synthetic.main.fm_review_tablayout.*
import win.zwping.code.utils.LogUtil
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-08 10:58:43 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class PTabLayoutFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_review_tablayout

    override fun doBusiness() {
        val list = listOf(Bean("选中文字变大", R.mipmap.page_state_empty, R.mipmap.ic_launcher_round), Bean("通过API", R.mipmap.page_state_empty, R.mipmap.ic_launcher_round), Bean("动态控制Tab样式", R.mipmap.page_state_empty, R.mipmap.ic_launcher_round))
        simple_tab_layout
                ?.setCViewTC(Bean(),
                        R.layout.item_tab_layout,
                        {
                            it?.helper?.setImageResource(R.id.iv, it.item?.unSelectImg!!)?.setTextColor(R.id.tv, Color.GRAY)?.setText(R.id.tv, it.item.txt)
                            it?.helper?.getView<TextView>(R.id.tv)?.textSize = 12f
                        },
                        {
                            it?.helper?.setImageResource(R.id.iv, it.item?.selectImg!!)?.setTextColor(R.id.tv, Color.DKGRAY)
                            it?.helper?.getView<TextView>(R.id.tv)?.textSize = 18f
                        }
                )?.data = list

        LogUtil.i("content size : ${simple_tab_layout?.getCViewTC<Bean>()?.data?.size}")
    }

    data class Bean(val txt: String? = null, val selectImg: Int = 0, val unSelectImg: Int = 0)
}