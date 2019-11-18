package win.zwping.pseries.fm.review

import kotlinx.android.synthetic.main.fm_recycler_view.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm
import win.zwping.pseries.fm.review.prv.*

/**
 *
 * describe：
 *     note：
 *  @author：zwp on 2019-03-15 11:49:05 mail：1101558280@qq.com web: http://www.zwping.win
 */
class PRecyclerViewFm : BaseFm() {

    override fun setIsLazy() = false
    override fun bindChildLayout() = R.layout.fm_recycler_view

    override fun doBusiness() {
        pvp?.setAdapterFm(childFragmentManager,
                listOf( LinearPRvFm(), GridPRvFm(), SectionPRvFm(), StickPRvFm(), DecorationPRvFm(), AutoLoadMorePRvFm(),AutoLoadMoreOfRefreshPRvFm()),
                ptl,
                listOf( "线性", "网格", "分组", "吸顶", "分割线", "自动加载", "自动加载(SmartRefresh)"))
//        pvp?.setAdapterFmOfBanner(childFragmentManager,
//                listOf(AutoLoadMorePRvFm(), LinearPRvFm(), GridPRvFm(), SectionPRvFm(), StickPRvFm(), DecorationPRvFm(), AutoLoadMorePRvFm(), LinearPRvFm())
//        ) { position, total -> showToast("$position----$total") }

//        pvp?.startAutoPlay(lifecycle, 3)
    }
}