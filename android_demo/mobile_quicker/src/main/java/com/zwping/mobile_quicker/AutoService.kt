package com.zwping.mobile_quicker

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 *
 * zwping @ 5/11/21
 */
class AutoService : AccessibilityService() {

    companion object {

    }

    init {
//        serviceInfo = AccessibilityServiceInfo().apply {
//            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
//            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
//            canRetrieveWindowContent
//            packageNames = arrayOf("com.android.settings")
//            notificationTimeout = 100
//        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        // performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
    }

    private var findXianzhi = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Utils.Logi("接收到系统发送AccessibilityEvent时的回调: $event")
        rootInActiveWindow?.apply {
            if (findAccessibilityNodeInfosByText("开发者选项").size == 0) return
            findXianzhi = findAccessibilityNodeInfosByText("后台进程限制").size != 0
//            findAccessibilityNodeInfosByText("进程限制").forEach {
//                var clickNode = it
//                while (null != clickNode) {
//                    Utils.Logi("--${clickNode.isCheckable}")
//                    if (clickNode.isCheckable) {
//                        Utils.Logi("${clickNode.isCheckable} - ${clickNode}")
//                        break
//                    }
//                    clickNode = clickNode.parent
//                }
//                return
//            }
            for (i in 0 until childCount) {
                getChild(i).also { rv ->
                    if (rv.className.contains("RecyclerView")) {
                        if (!findXianzhi) rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                        return
                    }
                }
            }
            return
            Utils.Logi("接收到系统发送AccessibilityEvent时的回调: $event")
            findAccessibilityNodeInfosByText(("后台进程限制"))?.forEach {
                Utils.Logi(it.text)
            }
        }
    }

    override fun onInterrupt() {
        Utils.Logi("服务中断时的回调")
    }
}