package win.zwping.pseries

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import win.zwping.code.cview.StateLayout
import win.zwping.code.review.PProgressBar
import win.zwping.code.utils.FragmentUtil.replace
import win.zwping.code.utils.HandlerUtil
import win.zwping.code.utils.LogUtil
import win.zwping.code.utils.SDCardUtil
import win.zwping.frame.comm.CommPop
import win.zwping.pseries.base.BaseAc
import win.zwping.pseries.base.BaseBean
import win.zwping.pseries.base.BaseFm
import win.zwping.pseries.fm.cview.*
import win.zwping.pseries.fm.review.*
import java.util.ArrayList

class MainActivity : BaseAc() {

    override fun initData(intent: Intent?) {
    }

    override fun bindLayout() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        setAutoHideKB()
        prv?.setAdapterSup(String(), android.R.layout.simple_list_item_1) {
            it?.helper?.setText(android.R.id.text1, it.item)
                    ?.setBackgroundColor(android.R.id.text1, Color.parseColor("#dedede"))
        }
        BaseBean()
        StateLayout.wrap(this).apply {
            init { }
            HandlerUtil.runOnUiThreadDelay({ showContentView() }, 1000)
        }

        val pro = PProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        pro.setHorizontalParams(Color.RED, Color.YELLOW)
//        pro.secondaryProgress = Color.RED
//        pro.setBackgroundColor(Color.BLACK)
        pro.progress = 10; pro.max = 100


//        CommPop(this)
//                .setShowExit()
//                .setContentView(pro)
//                .setConfirmLis { pop, s -> showToast(s) }.show()


        val list = ArrayList<List<Bean>>()

        var list1: MutableList<Bean> = ArrayList<Bean>()
        for (i in 0..15) {
            if (list1.size == 10) {
                list.add(list1)
                list1 = ArrayList<Bean>()
            } else if (i + 1 == 16) {
                list.add(list1)
            }
            list1.add(Bean("title$i"))
        }

    }

    internal inner class Bean(var title: String)

    override fun doBusiness() {
        val list = listOf("箭头控件(ArrowView)", "自定义Bar(替换ToolBar)", "文本框计数控件(EtWordCountLayout)", "自定义MenuBar(菜单Bar)", "超级背景控件(SupBgLayout)", "页面状态切换控件(StateLayout)", " ",
                "TabLayoutCustomView定制化", "Button复写", "ViewPager复写", "RecyclerView复写", "EditText复写", "CardView复写", "视图简单的状态颜色切换", "WebView重写", "ProgressBar重写", "PImageView复写", "未读消息")
        prv?.setNewData(list)
        prv?.adapterSup?.setOnItemClickListener { adapter, view, position ->
            val it = prv.adapterSup.data[position].toString()
            when (it) {
                "文本框计数控件(EtWordCountLayout)" -> replace(it, EtWordCountLayoutFm())
                "超级背景控件(SupBgLayout)" -> replace(it, SupBgLayoutFm())
                "页面状态切换控件(StateLayout)" -> replace(it, StateLayoutFm())
                "箭头控件(ArrowView)" -> replace(it, ArrowViewFm())
                "自定义Bar(替换ToolBar)" -> replace(it, BarFm())
                "自定义MenuBar(菜单Bar)" -> replace(it, MenuBarFm())
                "TabLayoutCustomView定制化" -> replace(it, PTabLayoutFm())
                "Button复写" -> replace(it, PButtonFm())
                "ViewPager复写" -> replace(it, PViewPagerFm())
                "RecyclerView复写" -> replace(it, PRecyclerViewFm())
                "EditText复写" -> replace(it, PetFm())
                "CardView复写" -> replace(it, PCardViewFm())
                "视图简单的状态颜色切换" -> replace(it, ViewStateColorSwitchFm())
                "WebView重写" -> replace(it, PWebViewFm())
                "ProgressBar重写" -> replace(it, PProgressBarFm())
                "PImageView复写" -> replace(it, PIvFm())
                "未读消息" -> replace(it, BadgeViewFm())
                "未读消息" -> replace(it, BadgeViewFm())
                "未读消息" -> replace(it, BadgeViewFm())
                "未读消息" -> replace(it, BadgeViewFm())
                "未读消息" -> replace(it, BadgeViewFm())
                "未读消息" -> replace(it, BadgeViewFm())
                "未读消息" -> replace(it, BadgeViewFm())
            }
        }
        prv?.adapterSup?.setOnItemLongClickListener { baseQuickAdapter, view, position ->
            val it = prv.adapterSup.data[position].toString()
            when (it) {
                "页面状态切换控件(StateLayout)" -> replace(it, SwitchPageStateLayoutFm())
            }
            true
        }

        psv?.setOnLoadMoreLis {
//            prv?.getAdapterSup()?.loadMoreViewCount()
            println(123)
        }
        prv.getAdapterSup().loadMoreEnd(true)
    }

    private fun replace(title: String, fm: BaseFm) {
        setTitle(title)
        fm.setOnDestroyLis(View.OnClickListener { setTitle("pseries"); over_scroll_layout?.setOverScroll(true) })
        replace(supportFragmentManager, fm, R.id.frame_layout, true)
    }

    fun setOver() {
        over_scroll_layout?.setOverScroll(false)
    }

}
