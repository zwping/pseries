package com.example.plib_junit

import win.zwping.code.utils.ConversionUtil.*
import org.junit.Test

class ConversionUtil {

    private val millis: Long by lazy { System.currentTimeMillis() }

    @Test
    fun millis2FitTimeSpan() {
        val m = 123312L
        println(millis2FitTimeSpan(m, 1))
    }

    @Test
    fun millis2FitUSTimeSpan() {

    }


}