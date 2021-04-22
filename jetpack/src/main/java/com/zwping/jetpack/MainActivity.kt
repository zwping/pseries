package com.zwping.jetpack

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.zwping.jetpack.ktxs.setOnDebounceClickListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import kotlin.experimental.and

// TODO: 2020/11/12 当前界面改造为mvvm模式
@Route(path = "/jetpack/main")
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.test(ly_container)
//        startActivity(Intent(this, ViewModelAc::class.java).apply { putExtra("ids", "value1") })
//        startA(DiffUtilAc::class.java)
        // start("/jetpack/retrofit")
        ly_container?.also {
            it.addView(cb().apply { text = "Toolbar";setOnDebounceClickListener { start("/jetpack/toolbar") } })
            // @Deprecated("常见模式中回执需要在onResume中接收, 可直接以viewModel形式替代")
            // it.addView(cb().apply { text = "Activity Result";setOnDebounceClickListener { } }) // https://mp.weixin.qq.com/s/lWayiBS4T4EHcsUIgnhJzA
            it.addView(cb().apply { text = "IDiffUtilCall";setOnDebounceClickListener { start("/jetpack/idiffutil") } })
            it.addView(cb().apply { text = "Java2XmlSelectedAc";setOnDebounceClickListener { start("/jetpack/java2xmlselected") } })
            // it.addView(cb().apply { text = "Broadcast Receiver";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "ARouter";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Retrofit";setOnDebounceClickListener { start("/jetpack/retrofit") } })
            it.addView(cb().apply { text = "ViewPager2";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "ViewModel";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "DataStore";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Room";setOnDebounceClickListener { } })
            // it.addView(cb().apply { text = "Paging3";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Fragment";setOnDebounceClickListener { } })
            it.addView(cb().apply { text = "Coroutines";setOnDebounceClickListener { } })
        }

//        encoder()
//        decoder()

//        ch(listOf(NameValuePair("dno","f2ddfe75e68dad7c62eba8754ca6d48dcc"),
//                NameValuePair("page", "1"),
//                NameValuePair("per_page", "10"),
//                NameValuePair("net", "wifi"),
//                NameValuePair("man", "OPPO"),
//                NameValuePair("mod", "OPPO R9 Plusm A"),
//                NameValuePair("pf", "android"),
//                NameValuePair("pf_ver", "5.1.1"),
//                NameValuePair("verc", "32"),
//                NameValuePair("ver", "1.4.6"),
//                NameValuePair("fr", "market_huawei_01"),
//                NameValuePair("an", "3.0"),
//                NameValuePair("code", "yikaobiguo"),
//                NameValuePair("tv", "0"),
//                NameValuePair("tz", "+8"),
//                NameValuePair("lang", "zh"),
//                NameValuePair("sid", "769437s5ffdebf5aa7ed329bf9107fd94265693f7")))



        Thread{
            val image = "https://yikao-app.oss-cn-beijing.aliyuncs.com/21030517011537.png?Expires=1614936663&OSSAccessKeyId=TMP.3KdssmvTse8C2hQ7F7PgUG8YbZosonmJiib1d9jjX7NLgErbgzA5rmApgt4Hrq2nXr634WFpd5frWAq2AsRPs2oWQPFELj&Signature=yGsT6lbBC7Fo7v1FV%2FN8tJwiXyA%3D"
            val f = Glide.with(this).asFile().load(image).override(60).submit();
            val file = f.get()
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            Log.e("TAG", bitmap.width.toString() + "---" + bitmap.height + "---" + file.length())
        }.start()

    }

    data class NameValuePair(var name:String, var value:String)

    private fun ch(list: List<NameValuePair>) {
        val strArr = arrayOfNulls<String>(list.size)
        for (i in 0 until list.size) {
            val nameValuePair: NameValuePair = list.get(i)
            strArr[i] = nameValuePair.name + "=" + nameValuePair.value
        }
        println("${strArr[0]}")
        println("${strArr[1]}")
        Arrays.sort(strArr)
        println("${strArr[0]}")
        println("${strArr[1]}")
        val stringBuffer = StringBuffer()
        for (i2 in strArr.indices) {
            stringBuffer.append(strArr[i2])
            if (i2 != strArr.size - 1) {
                stringBuffer.append("&")
            }
        }

        println(stringBuffer.toString())
        println("----${md5((md5(stringBuffer.toString())))}")


    }

    /**
     * md5加密字符串
     * md5使用后转成16进制变成32个字节
     */
    fun md5(str: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(str.toByteArray())
        //没转16进制之前是16位
        println("result${result.size}")
        //转成16进制后是32字节
        return toHex(result)
    }

    fun toHex(byteArray: ByteArray): String {
        val result = with(StringBuilder()) {
            byteArray.forEach {
                val hex = it.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                if (hexStr.length == 1) {
                    this.append("0").append(hexStr)
                } else {
                    this.append(hexStr)
                }
            }
            this.toString()
        }
        //转成16进制后是32字节
        return result
    }

    private fun md55(str: String): String{
        return try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            instance.update(str.toByteArray())
            val stringBuffer = StringBuffer()
            val digest: ByteArray = instance.digest()
            for (b in digest) {
                var i = b.toInt()
                if (b < 0) {
                    i = b + 256
                }
                if (i < 16) {
                    stringBuffer.append("0")
                }
                stringBuffer.append(Integer.toHexString(if (i == 1) 1 else 0))
            }
            stringBuffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }

    private fun cb(): AppCompatButton = AppCompatButton(this).apply { isAllCaps = false; gravity = Gravity.START or Gravity.CENTER_VERTICAL }

    private fun start(path:String){
        ARouter.getInstance().build(path).navigation()
    }


//    fun decoder() {
//         val str = "VlXfieaFXo+oV84YRvoOf2Cl5B/d/lPfDy47AxD59NTPqQnkEmdiZe2H1WRL7dU6BupChoj/cBaDnHU8cY7Tv7zF3OjAwjhwXvQPVeTAI/zJNbSukSfXmB7pOQ5EXn7B9dPNjKJIAxVCMwyz9o+lWKv8/UWi5jEYnor8FJlunDJuViKQOdSgNOrAL8tk4lnnLxc8WXxz/HpgK4YPCkdjv+QPDSXjxxRjsaiMMTtikvbHDRVvz1IWGUDHz5xW2spdWLvMcfTI8DM+sDqbgokRLKa43j0En8HMcMxc5HkZkJojxuMed4H5BE4Qz4kd5QR0E4G7c4QKUgJJsS9Xo27JmYba6/MlV3NC"
////        val str = "ejLLGyHFCWLWIWZO496f3Q=="
//        val d = d(2, Base64.decode(str, 2))
//        println(String(d))
//    }

    fun decoder() {
    // 响应值的解析 √
        val str = "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"
        val d = d(2, Base64.decode(str, 2))
        println(String(d))
    }


    fun encoder() {
        try {
            val str = "待加密的值"
            val d = d(1, str.toByteArray())
            val b64 = Base64.encodeToString(d, Base64.NO_WRAP)
            val r = URLEncoder.encode(String(b64.toByteArray()), "UTF-8")
            println("$r")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun d(i: Int, str: ByteArray): ByteArray {
        val c = Cipher.getInstance("DES")
//        val c = Cipher.getInstance("DES/CBC/PKCS5Padding")
        val key = "fb16ce38".toByteArray()
        c.init(i, SecretKeyFactory.getInstance("DES").generateSecret(DESKeySpec(key)))
        return c.doFinal(str)
    }


}