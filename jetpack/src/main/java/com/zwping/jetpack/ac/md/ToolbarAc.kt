package com.zwping.jetpack.ac.md

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ktx.immersionBar
import com.zwping.jetpack.R
import com.zwping.jetpack.ktxs.*
import com.zwping.jetpack.showToast
import kotlinx.android.synthetic.main.ac_tool_bar.*

/**
 * <pre>
 * describe : 借[https://github.com/getActivity/TitleBar]打样实现toolbar的实现
 * note     :
 * author   : zwping
 * date     : 2020/10/26 2:10 PM
 * email    : 1101558280@qq.com
 * </pre>
 */
@Route(path = "/jetpack/toolbar")
class ToolbarAc : AppCompatActivity() {

    //
    // setSupportActionBar
    // toolbar + CollapsingToolbarLayout + AppBarLayout + CoordinatorLayout实现滑动toolbar渐变效果
    // 默认toolbar
    // addView形势实现居中标题
    // childCount遍历实现标题居中
    // 代码中实现自定义menu
    // 代码中实现menu小红点
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionBar {}
        setContentView(R.layout.ac_tool_bar)

        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tool_bar?.setStatusBarImmersion()
        // tool_bar?.addMenu(0x01,R.drawable.ic_baseline_android_24,"临时加")
        // tool_bar?.inflateMenu(R.menu.menu_tool_bar)
        tool_bar?.setOnMenuItemClickListener { showToast("$it ${it.itemId}"); true }
        tool_bar?.setNavigationOnClickListener { finish() }

        ly_container?.post {
            initExample(ly_container)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.addMenu(0x01, R.drawable.ic_baseline_android_24, "临时加")
        menu?.addMenu(0x02, R.drawable.ic_baseline_search_24, "搜索")
        menu?.addMenu(0x03, R.drawable.ic_baseline_settings_24, "设置")
        // menuInflater.inflate(R.menu.menu_tool_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        @JvmStatic
        fun initExample(container: ViewGroup?) {
            val ctx by lazy { container!!.context }
            container?.also {
                it.addView(
                        Toolbar(ctx).apply {
                            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                            setLogo(R.drawable.ic_baseline_android_24)
                            title = "默认toolbar"
                            subtitle = "副标题"
                            addMenu(0x01, R.drawable.ic_baseline_android_24, "临时加")
                            inflateMenu(R.menu.menu_tool_bar)

                            setOnMenuItemClickListener { ctx.showToast("$it ${it.itemId}");true }
                            setNavigationOnClickListener { ctx.showToast("$it") }
                        })

                it.addView(
                        Toolbar(ctx).apply {
                            title = "默认标题"

                            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                        }
                )

                it.addView(
                        Toolbar(ctx).apply {
                            val tv = setTitleOfCenter("居中标题")
//                        val tv = setTitleOfCenter("居中标题居中标题居中标题居中标题居中标题居中标题居中标题居中标题居中标题居中标题")

                            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                        }
                )

                it.addView(
                        Toolbar(ctx).apply {
                            val tv = setTitleOfCenter("居中标题")

                            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                            setLogo(R.drawable.ic_baseline_android_24)
                            inflateMenu(R.menu.menu_tool_bar)
                        }
                )

                it.addView(
                        Toolbar(ctx).apply {
                            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

                            setTitleCenter("标题居中")
//                        setTitleCenter("标题居中标题居中标题居中标题居中标题居中标题居中标题居中标题居中标题居中标题居中")

                        }
                )

                it.addView(
                        Toolbar(ctx).apply {
                            setTitleCenter("标题居中")

                            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                            setLogo(R.drawable.ic_baseline_android_24)
                            inflateMenu(R.menu.menu_tool_bar)
                        }
                )


                it.addView(
                        Toolbar(ctx).apply {
                            title = "自定义menu"

                            addMenu(0x01, R.drawable.ic_baseline_search_24, "menu1")
                            addMenu(0x02, R.drawable.ic_baseline_search_24, "menu2")
                            addMenu(0x03, R.drawable.ic_baseline_search_24, "menu3")
                            addMenu(0x04, R.drawable.ic_baseline_search_24, "menu4")
                            menu.findItem(0x01).icon.setTint(Color.RED)

                            setOnMenuItemClickListener { ctx.showToast("$it ${it.itemId}");true }
                        }
                )

                it.addView(
                        Toolbar(ctx).apply {
                            title = "自定义menu - 小红点"

                            addMenuBadge(0x01, R.drawable.ic_baseline_search_24, "menu1") { ctx.showToast("${it.item?.itemId}--${it.num}") }
                            addMenuBadge(0x02, R.drawable.ic_baseline_search_24, "menu2") { ctx.showToast("${it.item?.itemId}--${it.num}") }
                            addMenuBadge(0x03, R.drawable.ic_baseline_search_24, "menu3") { ctx.showToast("${it.item?.itemId}--${it.num}") }
                            menu.findItem(0x01).icon.setTint(Color.RED)

                            getActionProvider2(0x01)?.setOnMenuItemClickListener { ctx.showToast("另外一处") }?.setBadgeNum(null)
                            getActionProvider2(0x02)?.setBadgeNum(999)
                            getActionProvider2(0x03)?.badgeView?.apply {
                                val w = (5 * context.resources.displayMetrics.density).toInt()
                                layoutParams = layoutParams.apply {
                                    width = w;height = w
                                }
                                color = Color.GREEN
                            }
                            getActionProvider2(0x03)?.setBadgeVisible(true)

                            setOnMenuItemClickListener { ctx.showToast("$it ${it.itemId}");true }
                        }
                )

                it.addView(
                        Toolbar(ctx).also {
                            it.title = "底线 - 需外部实现..."
                            it.setTitleTextColor(Color.GRAY)
                        })

                it.addView(
                        AppCompatTextView(ctx).also {
                            it.text = "Java使用toolbar ktx"
                            it.setOnClickListener { ARouter.getInstance().build("/jetpack/toolbarj").navigation() }
//                            it.setOnClickListener { it.context.startActivity(Intent(it.context, ToolbarJAc::class.java)) }
                        }
                )


                it.addView(
                        View(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 1500)
                        }
                )
            }
        }
    }
}