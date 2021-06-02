package com.zwping.plibx;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.Timer;
import java.util.TimerTask;

/**
 * zwping @ 4/23/21
 */
public class ITimerJ implements LifecycleEventObserver {

    public static ITimerJ INSTANCE(@NonNull OnActionLis lis, long delay) { return INSTANCE(lis, delay, 0); }

    public static ITimerJ INSTANCE(@NonNull OnActionLis lis, long delay, long period) { return new ITimerJ(lis, delay, period); }

    public ITimerJ(@NonNull OnActionLis lis, long delay, long period) {
        this.lis = lis;
        this.delay = delay;
        this.period = period;
    }

    public int count = 0;

    private OnActionLis lis;
    private long delay;     // milliseconds 延迟时间
    private long period;   // milliseconds 周期时间; =0 =单次倒计时任务
    private Timer timer;
    private TimerTask task;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int uEvent;
    private OnLifecycleChange onLifecycleChange;

    public ITimerJ setOnLifecycleChange(OnLifecycleChange lis) { this.onLifecycleChange = lis; return this; }

    public ITimerJ schedule(LifecycleOwner owner, @IntRange(from = 0, to = 2) int uEvent) {
        if (null != owner) {
            owner.getLifecycle().removeObserver(this);
            owner.getLifecycle().addObserver(this);
        }
        cancel();
        count = 0;
        initTimer();
        this.uEvent = uEvent;
        if (0L == period) { timer.schedule(task, delay); } // 单次执行计划
        else { timer.schedule(task, delay, period); }
        return this;
    }

    public ITimerJ schedule(LifecycleOwner owner) { return schedule(owner, 0); }

    @Deprecated
    public ITimerJ schedule() { return schedule(null); }

    public void cancel() {
        if (null != timer) { timer.cancel(); timer.purge(); }
        if (null != task) { task.cancel(); }
        timer = null;
        task = null;
    }

    private void initTimer() {
        if (null == timer) { timer = new Timer(); }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    action();
                }
            };
        }
    }

    private void action() {
        handler.post(() -> {
            ++count;
            lis.action(this);
        });
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (null != onLifecycleChange) { onLifecycleChange.onEvent(this, event); }
        if (0 == uEvent && event == Lifecycle.Event.ON_DESTROY) {
            cancel();
        } else if (1 == uEvent && event == Lifecycle.Event.ON_STOP) {
            cancel();
        } else if (2 == uEvent && event == Lifecycle.Event.ON_PAUSE) {
            cancel();
        }
    }

    /* ================== */

    public interface OnActionLis{ void action(ITimerJ it); }

    public interface OnLifecycleChange{ void onEvent(@NonNull ITimerJ it, @NonNull Lifecycle.Event event); }

}