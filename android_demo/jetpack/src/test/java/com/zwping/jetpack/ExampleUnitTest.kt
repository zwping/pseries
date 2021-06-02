package com.zwping.jetpack

import android.util.Base64
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URLDecoder
import java.net.URLEncoder
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun decoder() {
        val str = "VlXfieaFXo%2BoV84YRvoOf2Cl5B%2Fd%2FlPfDy47AxD59NTPqQnkEmdiZe2H1WRL7dU6BupChoj%2FcBaDnHU8cY7Tv7zF3OjAwjhwXvQPVeTAI%2FzJNbSukSfXmB7pOQ5EXn7B9dPNjKJIAxVCMwyz9o%2BlWKv8%2FUWi5jEYnor8FJlunDJuViKQOdSgNOrAL8tk4lnnLxc8WXxz%2FHpgK4YPCkdjv%2BQPDSXjxxRjsaiMMTtikvbHDRVvz1IWGUDHz5xW2spdWLvMcfTI8DM%2BsDqbgokRLKa43j0En8HMcMxc5HkZkJojxuMed4H5BE4Qz4kd5QR0E4G7c4QKUgJJsS9Xo27JmYba6%2FMlV3NC"
        val d = URLDecoder.decode(str, "UTF-8")
        val b64 = Base64.decode(d, 2)
        println(b64)
    }


    @Test
    fun encoder() {
        try {
            val str = "待加密的值"
            val des = des(1, str.toByteArray(), "fb16ce38".toByteArray())
            val b64 = Base64.encodeToString(des, 2)
            val r = URLEncoder.encode(String(b64.toByteArray()), "UTF-8")
            println(r)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun des(i: Int,str: ByteArray, key: ByteArray): ByteArray{
        val c = Cipher.getInstance("DES")
        c.init(i, SecretKeyFactory.getInstance("DES").generateSecret(DESKeySpec(key)))
        return c.doFinal(str)
    }
}

