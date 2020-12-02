package win.zwping.pseries.base

import android.graphics.Color
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import win.zwping.code.basic.BasicApp
import win.zwping.code.cview.StateLayout
import win.zwping.pseries.R

/**
 * describe：
 *     note：
 *  @author：zwp on 2019-03-04 17:15:00 mail：1101558280@qq.com web: http://www.zwping.win
 */
class App : BasicApp() {
    override fun init() {
        // StateLayout.appInit(loadingLayoutId = R.layout.comm_pop_loading,animDuration = 0)
    }

    override fun debugInit() {
    }


    //static 代码段可以防止内存泄露
    companion object {

        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(android.R.color.black, android.R.color.white)//全局设置主题颜色
                ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }

}
