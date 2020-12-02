package com.zwping.jetpack

import android.content.Context
import android.view.View
import android.widget.Checkable
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

//<editor-fold desc="View扩展">

/** View防抖点击 */
inline fun <V : View> V.setOnDebounceClickListener(time: Long = 500L, crossinline lambda: (V) -> Unit) {
    var lastTime = 0L
    setOnClickListener {
        val curTime = System.currentTimeMillis()
        if (curTime - lastTime > time || this is Checkable) { // 0.5秒只能点击一次
            lastTime = curTime
            lambda(this)
        }
    }
}

/*** View双击 ***/
inline fun <V : View> V.setOnDoubleClickListener(
        intervalTime: Long = 500L,
        crossinline secondClickListener: (V) -> Unit) {
    var lastTime = 0L
    setOnClickListener {
        val curTime = System.currentTimeMillis()
        if (curTime - lastTime > intervalTime) {
            lastTime = curTime
        } else {
            lastTime = 0L
            secondClickListener(this)
        }
    }
}

//</editor-fold>


/*** Toast ***/
fun Context?.showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

//<editor-fold desc="资源转换扩展">

//fun @receiver:ColorInt Int.toColor(ctx: Context?): Int? {
//    ctx?.also {
//        return ContextCompat.getColor(ctx, this)
//    }
//    return null
//}


//</editor-fold>
