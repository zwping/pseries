package com.zwping.mobile_quicker

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

/**
 *
 * zwping @ 5/11/21
 */
object Utils {

    fun Logi(msg: Any) { Log.i("zz", "$msg")}


    /*** 无障碍服务开启状态 ***/
    fun isAccessibilitySettingsOn(mContext: Context, clazz: Class<out AccessibilityService?>): Boolean {
        var accessibilityEnabled = 0
        val service: String = mContext.packageName.toString() + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) { e.printStackTrace() }
        val mStringColonSplitter: TextUtils.SimpleStringSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(mContext.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService: String = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    fun isAdbEnable(ctx: Context?): Boolean = Settings.Secure.getInt(ctx?.contentResolver, Settings.Global.ADB_ENABLED, 0) > 0


}