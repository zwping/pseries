package win.zwping.pseries.fm.review.prv

import android.graphics.Color
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.SectionEntity
import kotlinx.android.synthetic.main.fm_prv_linear.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm
import java.io.Serializable

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-19 11:11:11 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class SectionPRvFm : BaseFm() {

    override fun setIsLazy() = false
    override fun bindChildLayout() = R.layout.fm_prv_linear

    override fun doBusiness() {
        val list = arrayListOf<Bean>()
        for (i in 1..10) {
            list.add(Bean(true, "$i--"))
            for (j in 1..5) {
                list.add(Bean(B("$j")))
            }
        }

        prv?.adapter = Adapter(android.R.layout.simple_list_item_1, android.R.layout.simple_list_item_1, list)
    }

    class Bean : SectionEntity<B> {
        constructor(isHeader: Boolean, header: String?) : super(isHeader, header)
        constructor(t: B?) : super(t)
    }

    data class B(var s: String? = null) : Serializable

    class Adapter : BaseSectionQuickAdapter<Bean, BaseViewHolder> {

        constructor(layoutResId: Int, sectionHeadResId: Int, data: MutableList<Bean>?) : super(layoutResId, sectionHeadResId, data)

        override fun convertHead(helper: BaseViewHolder?, item: Bean?) {
            helper?.setBackgroundColor(android.R.id.text1, Color.YELLOW)?.setText(android.R.id.text1, item?.header)
        }

        override fun convert(helper: BaseViewHolder?, item: Bean?) {
            helper?.setBackgroundColor(android.R.id.text1, Color.GRAY)?.setText(android.R.id.text1, item?.t?.s)
        }
    }
}