package com.zwping.jetpack

import android.content.Context
import android.widget.Toast

/**
 *
 * zwping @ 1/11/21
 */
inline fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT)
}