package com.zwping.jetpack.ktxs

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 简化liveData -> data
 *  - 替代EventBus
 *
 * abstract class [ILiveData]
 *  class method [data] [observe]
 *
 * zwping @ 12/25/20
 */
abstract class ILiveData<B> {

    var liveData = MutableLiveData<B>()
        private set

    var data: B?
        set(value) {
            liveData.postValue(value)
        }
        get() = liveData.value

    /**
     * 观察者
     * @param owner
     * @param observer
     * @return [ILiveData]
     */
    fun observe(owner: LifecycleOwner?, observer: (B?) -> Unit): ILiveData<B> {
        owner?.also { liveData.observe(it, Observer { observer(data) }) }
        return this
    }

}