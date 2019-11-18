package win.zwping.pseries.base

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fm_base.*
import win.zwping.code.basic.BasicFm
import win.zwping.code.utils.FragmentUtil
import win.zwping.pseries.R

/**
 * describe：
 *     note：
 *  @author：zwp on 2019-03-04 17:14:24 mail：1101558280@qq.com web: http://www.zwping.win
 */
abstract class BaseFm : BasicFm() {

    override fun bindLayout() = R.layout.fm_base

    override fun initView(savedInstanceState: Bundle?) {
        root_layout?.setOnClickListener { }
        floating_btn?.setOnClickListener { lis?.onClick(null);FragmentUtil.remove(this) }
        inflater.inflate(bindChildLayout(), root_layout)
        if (hideFActionButton()) floating_btn?.visibility = View.GONE
    }

    abstract fun bindChildLayout(): Int


    open fun hideFActionButton() = false


    //////////////// Sample 监听 //////////////////////

    override fun onDestroy() {
        lis?.onClick(null)
        super.onDestroy()
    }

    fun setOnDestroyLis(lis: View.OnClickListener) {
        this.lis = lis
    }

    private var lis: View.OnClickListener? = null

}
