package win.zwping.pseries.fm.review.prv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zwping.plibx.BaseAdapter
import com.zwping.plibx.BaseVH
import kotlinx.android.synthetic.main.fm_rv_grid2.*
import win.zwping.pseries.R
import win.zwping.pseries.base.BaseFm
import win.zwping.pseries.databinding.ItemRvGrid21Binding

/**
 *
 * zwping @ 5/10/21
 */
class RvGridSpanSizeFm : BaseFm() {

    override fun setIsLazy(): Boolean { return false }
    override fun bindChildLayout(): Int = R.layout.fm_rv_grid2

    override fun doBusiness() {
        val data = mutableListOf<Bean>().apply {
            add(Bean(1, "1"))
            add(Bean(2, "2"))
            add(Bean(2, "2"))
            add(Bean(3, "3"))
            add(Bean(3, "3"))
            add(Bean(3, "3"))
            add(Bean(4, "4"))
            add(Bean(4, "4"))
            add(Bean(4, "4"))
            add(Bean(4, "4"))
        }

        rv.layoutManager = GridLayoutManager(context, 5).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when(data[position].type){
                        1 -> 4
                        2 -> 2
                        3 -> 1
                        else -> 1
                    }
                }
            }
        }
//        rv.adapter = adapter
//        adapter.data = data
    }

//    private val adapter by lazy {
//        object : BaseAdapter<Bean>() {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<ViewBinding, Bean> {
//                return VH1(ItemRvGrid21Binding.inflate(LayoutInflater.from(parent.context), parent, false))
//            }
//
//
//            inner class VH1(vb: ItemRvGrid21Binding) : BaseVH<Bean, ItemRvGrid21Binding>(vb) {
//                override fun bind(entity: Bean) {
//                    super.bind(entity)
//                    this.vb.tvTitle.text = entity.value
//                }
//            }
//        }
//    }

    data class Bean(val type: Int, val value: String)


}