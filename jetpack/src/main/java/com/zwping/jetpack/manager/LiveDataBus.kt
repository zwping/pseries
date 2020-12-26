package com.zwping.jetpack.manager

import com.zwping.jetpack.ktxs.ILiveData

/**
 * live data bus manager
 * zwping @ 12/25/20
 */
class LiveDataBus {

    object TestBus : ILiveData<Int>()

}