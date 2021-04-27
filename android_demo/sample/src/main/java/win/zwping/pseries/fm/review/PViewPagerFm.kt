package win.zwping.pseries.fm.review

import android.graphics.Color
import android.widget.TextView
import kotlinx.android.synthetic.main.fm_review_view_pager.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm
import win.zwping.pseries.fm.review.vp.PItemViewPagerFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-13 17:07:15 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class PViewPagerFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_review_view_pager

    override fun doBusiness() {
        pvp1?.setAdapterFm(childFragmentManager, listOf(PItemViewPagerFm().setP(0), PItemViewPagerFm().setP(1), PItemViewPagerFm().setP(2)))

        pvp2?.setAdapterFm(childFragmentManager,
                listOf(PItemViewPagerFm().setP(0), PItemViewPagerFm().setP(1), PItemViewPagerFm().setP(2)),
                ptl,
                listOf("0", "1", "2"))
        pvp3?.setAdapterFm(childFragmentManager,
                listOf(PItemViewPagerFm().setP(0), PItemViewPagerFm().setP(1), PItemViewPagerFm().setP(2)),
                pt3)

        val list = listOf(PTabLayoutFm.Bean("选中文字变大", R.mipmap.page_state_empty, R.mipmap.ic_launcher_round), PTabLayoutFm.Bean("通过API", R.mipmap.page_state_empty, R.mipmap.ic_launcher_round), PTabLayoutFm.Bean("动态控制Tab样式", R.mipmap.page_state_empty, R.mipmap.ic_launcher_round))
        pt3?.setCViewTC(PTabLayoutFm.Bean(),
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
    }

}