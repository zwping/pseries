package com.zwping.jetpack.ktxs

import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*

/**
 * 定时器扩展
 * - 生命周期感知
 * - 单次计划执行
 * - 循环执行
 *
 * public class [ITimer]
 *  class method [count] [schedule] [cancel]
 *
 * @param action
 * @param delay milliseconds 延迟时间
 * @param period milliseconds 周期时间; =0 =单次倒计时任务
 *
 * zwping @ 12/24/20
 */
class ITimer(private val action: (ITimer) -> Unit, private val delay: Long, private val period: Long = 0) {
    /*** 执行次数 ***/
    var count = 0
        private set

    private var timer: Timer? = null
    private var task: TimerTask? = null

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * 执行
     * @param owner
     * @param lifecycle 0 onDestroy / 1 onStop / 2 onPause
     */
    fun schedule(owner: LifecycleOwner?, @IntRange(from = 0, to = 2) lifecycle: Int = 0): ITimer {
        cancel()
        count = 0
        initTimer()
        autoDisposable(owner, lifecycle)
        if (0L == period) timer?.schedule(task, delay) // 单次计划执行
        else timer?.schedule(task, delay, period)
        return this
    }


    /*** 取消 ***/
    fun cancel() {
        timer?.apply { cancel();purge() }
        task?.apply { cancel() }
        timer = null
        task = null
    }

    /* ------------------------------ */

    private fun initTimer() {
        if (null == timer) {
            timer = Timer()
        }
        if (null == task) {
            task = object : TimerTask() {
                override fun run() {
                    handler.post {
                        ++count
                        action(this@ITimer)
                    }
                }
            }
        }
    }

    private fun autoDisposable(owner: LifecycleOwner?, @IntRange(from = 0, to = 2) lifecycle: Int) {
        owner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (0 == lifecycle) cancel()
                super.onDestroy(owner)
            }

            override fun onStop(owner: LifecycleOwner) {
                if (1 == lifecycle) cancel()
                super.onStop(owner)
            }

            override fun onPause(owner: LifecycleOwner) {
                if (2 == lifecycle) cancel()
                super.onPause(owner)
            }
        })
    }

}