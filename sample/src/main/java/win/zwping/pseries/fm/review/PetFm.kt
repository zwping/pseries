package win.zwping.pseries.fm.review

import kotlinx.android.synthetic.main.fm_pet.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-03-27 17:01:10 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
class PetFm : BaseFm() {

    override fun bindChildLayout() = R.layout.fm_pet

    override fun doBusiness() {

        pet?.setOnPswToggleListener { pet, show -> showToast(show) }
    }

}