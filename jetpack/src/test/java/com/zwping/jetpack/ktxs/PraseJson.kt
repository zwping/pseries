package com.zwping.jetpack.ktxs

import org.json.JSONObject
import org.junit.Test

/**
 *
 * zwping @ 12/8/20
 */
class PraseJson {

    @Test
    fun parse() {
        val res1 = """
        {'code': 200, 'msg': '成功', 'data': ['1', '2', , null, 5], }
    """.trimIndent()
        println("原始JSONObject ${JSONObject(res1)}")
        val b = Bean(JSONObject(res1))
        println("optJSONArrayBasic ${b.data}")

        val res2 = """
            {'data1': [
{'code': 200, 'msg': '成功', 'data': ['1', '2', , null, 5], },
null,
 ,
{'code': 201, 'msg': '成功', 'data': ['1', '2', , null, 5], }
]}
        """.trimIndent()
        println("原始JSONObject ${JSONObject(res2)}")
        val b1 = Bean(JSONObject(res2))
        println("optJSONArrayOrNull ${b1.data1}")
        b1.data1?.forEach {
            println(it.data)
        }

        val res3 = """
            {'code':200, 'sbean': {'code':200, 'msg': 'success'}}
        """.trimIndent()
        val b3 = Bean(JSONObject(res3))
        println("optJSONObjectOrNull ${b3.sbean?.msg}")
    }

    class Bean(obj: JSONObject? = null) : IJson(obj) {
        var code: Int? = null
        var msg: String? = null
        var data: MutableList<Int>? = null
        var data1: List<Bean>? = null
        var sbean: Bean? = null

        init {
            obj?.apply {
                code = optInt("code")
                msg = optString("msg")
                data = optJSONArrayBasic("data") { optInt(it) }
                data1 = optJSONArrayOrNull("data1") { Bean(it) }
                sbean = optJSONObjectOrNull("sbean") { Bean(it) }
            }
        }
    }
}