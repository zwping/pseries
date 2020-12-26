package com.zwping.jetpack.ktxs

import org.json.JSONArray
import org.json.JSONObject

/**
 * 标准化手动解析过程
 *  - 定制构造函数
 *  - 规避GSON无法解决的问题, 后台数据结构不稳定导致无法解析
 *
 * abstract class [IJson]
 *  class method [optJSONObjectOrNull] [optJSONArrayOrNull] [optJSONArrayBasic]
 * public method [optJSONObject2] [optJSONArray2]
 *
 * zwping @ 2020/9/25
 */
abstract class IJson(obj: JSONObject? = null) {

    /*** 手动解析对象 ***/
    fun <T : IJson> JSONObject.optJSONObjectOrNull(key: String, lis: (JSONObject) -> T): T? {
        optJSONObject(key)?.also { return lis.invoke(it) }
        return null
    }

    /*** 手动解析数组 ***/
    fun <T : IJson> JSONObject.optJSONArrayOrNull(key: String, lis: (JSONObject) -> T): MutableList<T>? {
        var data: MutableList<T>? = null
        optJSONArray(key)?.apply {
            data = mutableListOf()
            for (i in 0 until length()) {
                optJSONObject(i)?.also { data?.add(lis.invoke(it)) }
            }
        }
        return data
    }

    fun <BasicType> JSONObject.optJSONArrayBasic(key: String, lis: JSONArray.(Int) -> BasicType?): MutableList<BasicType>? {
        var data: MutableList<BasicType>? = null
        optJSONArray(key)?.apply {
            data = mutableListOf()
            for (i in 0 until length()) {
                lis.invoke(this, i)?.also { data?.add(it) }
            }
        }
        return data
    }

    companion object {
        @JvmStatic
        fun <T : IJson> optJSONObject2(obj: JSONObject?, lis: (JSONObject) -> T): T? {
            obj?.apply { return lis.invoke(obj) }
            return null
        }

        @JvmStatic
        fun <T : IJson> optJSONArray2(obj: JSONObject?, key: String?, lis: (JSONObject) -> T): MutableList<T>? {
            var data: MutableList<T>? = null
            obj?.optJSONArray(key)?.apply {
                data = mutableListOf()
                for (i in 0 until length()) {
                    optJSONObject(i)?.also { data?.add(lis.invoke(it)) }
                }
            }
            return data
        }
    }
}