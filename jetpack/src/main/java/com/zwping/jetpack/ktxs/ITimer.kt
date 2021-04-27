package com.zwping.jetpack.ktxs

import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*

/**
 * 定时器扩展
 *  - Timer类封装[ITimer], 执行[schedule]具有生命周期感知, 当[period]=0时为单次计划执行, 可获取执行次数[count], 同时可主动取消[count]
 *
 * @param action
 * @param delay milliseconds 延迟时间
 * @param period milliseconds 周期时间; =0 =单次倒计时任务
 *
 * zwping @ 12/24/20
 */
class ITimer(private val action: (ITimer) -> Unit, private val delay: Long, private val period: Long = 0): DefaultLifecycleObserver {
    /*** 执行次数 ***/
    var count = 0
        private set

    private var timer: Timer? = null
    private var task: TimerTask? = null
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private var uEvent = 0

    /**
     * 执行
     * @param owner
     * @param lifecycle 0 onDestroy / 1 onStop / 2 onPause
     */
    fun schedule(owner: LifecycleOwner?, @IntRange(from = 0, to = 2) lifecycle: Int = 0): ITimer {
        uEvent = lifecycle
        owner?.also { it.lifecycle.removeObserver(this); it.lifecycle.addObserver(this) }
        cancel()
        count = 0
        initTimer()
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
        if (null == timer) { timer = Timer() }
        if (null == task) {
            task = object : TimerTask() {
                override fun run() {
                    handler.post { ++count; action(this@ITimer) }
                }
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (0 == uEvent) { cancel() }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        if (1 == uEvent) { cancel() }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (2 == uEvent) { cancel() }
    }

}