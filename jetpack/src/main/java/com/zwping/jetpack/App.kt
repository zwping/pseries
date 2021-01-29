package com.zwping.jetpack

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.zwping.jetpack.ktxs.RetrofitFactory
import retrofit2.Retrofit

/**
 *
 * zwping @ 12/7/20
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (isAppDebug(this)) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)

        lifecycle()
    }


    private fun lifecycle() {
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                println("ac lifecycle callback = paused ${activity.javaClass.simpleName}")
            }

            override fun onActivityStarted(activity: Activity) {
                println("ac lifecycle callback = start ${activity.javaClass.simpleName}")

            }

            override fun onActivityDestroyed(activity: Activity) {
                println("ac lifecycle callback = destroyed ${activity.javaClass.simpleName}")

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                println("ac lifecycle callback = save state ${activity.javaClass.simpleName} $outState")

            }

            override fun onActivityStopped(activity: Activity) {
                println("ac lifecycle callback = stop ${activity.javaClass.simpleName}")

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                println("ac lifecycle callback = create ${activity.javaClass.simpleName}")

            }

            override fun onActivityResumed(activity: Activity) {
                println("ac lifecycle callback = resumed ${activity.javaClass.simpleName}")

            }

        })
    }

    /*** 获取APP是否Debug状态  */
    fun isAppDebug(ctx: Context): Boolean {
        return try {
            val packageName: String = ctx.packageName
            val pm: PackageManager = ctx.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}