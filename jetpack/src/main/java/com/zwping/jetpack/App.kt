package com.zwping.jetpack

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *
 * zwping @ 12/7/20
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        lifecycle()
    }


    private fun lifecycle() {
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                println("ac lifecycle callback = paused $activity")
            }

            override fun onActivityStarted(activity: Activity) {
                println("ac lifecycle callback = start $activity")

            }

            override fun onActivityDestroyed(activity: Activity) {
                println("ac lifecycle callback = destroyed $activity")

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                println("ac lifecycle callback = save state $activity $outState")

            }

            override fun onActivityStopped(activity: Activity) {
                println("ac lifecycle callback = stop $activity")

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                println("ac lifecycle callback = create $activity")

            }

            override fun onActivityResumed(activity: Activity) {
                println("ac lifecycle callback = resumed $activity")

            }

        })
    }
}