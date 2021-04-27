package com.zwping.jetpack.ac.md

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.zwping.jetpack.R
import com.zwping.jetpack.ktxs.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import java.util.concurrent.Executors


/**
 * RetrofitKtx Demo [RetrofitFactory]
 * zwping @ 12/28/20
 */
@Route(path = "/jetpack/retrofit")
class RetrofitAc : AppCompatActivity(R.layout.activity_main) {

    private val tvLog by lazy { AppCompatTextView(this) }

    @Synchronized
    private fun addLog(s: String?) {
        s?.also {
            tvLog.text = "${if (it.length > 40) "${it.substring(0, 40)}..." else s}\n${tvLog.text}"
        }
        println(s)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        RetrofitFactory.init { // 建议放在application中init
            baseUrl("https://v.juhe.cn/");
            it.addInterceptor(LogInterceptor(isAppDebug()))
        }
        super.onCreate(savedInstanceState)

        ly_container?.also {
            it.addView(tvLog)
        }

        get()

    }

    private val retrofit by lazy { RetrofitFactory.retrofit.create(DemoService::class.java) }

    private fun get() {
        retrofit.get(hashMapOf(Pair("key", "267d3fae8727e1c20b2130f71cfdc70e"), Pair("date", "1/1")))
                .enqueue2(this,
                        { addLog(it.body()?.string()) },
                        { addLog("get失败$it") },
                        { addLog("get start") },
                        { addLog("get end"); post() }
                )
    }

    private fun post() {
        retrofit.post("07e88b0c964a50e539ee999f1ca0bcad", "keji")
                .enqueue2(this@RetrofitAc,
                        { addLog(it.body()?.string()) },
                        { addLog("post失败$it") },
                        { addLog("post start") },
                        { addLog("post end"); down() }
                )
    }

    private val p1 by lazy { "${getExternalFilesDir("test")}/wechat800.apk" }
    private fun down() {
        val url = "https://dldir1.qq.com/weixin/android/weixin800android1840_arm64.apk"
        retrofit.download(url)
                .enqueue2(this@RetrofitAc,
                        {
                            Thread{
                                var p = 0
                                it.write2FileProgressOnThread(p1,
                                        {progress, total, done ->
                                            runOnUiThread {
                                                (progress * 100 / total).toInt().also {
                                                    if (it != p) {
                                                        p = it; addLog("down $p")
                                                    }
                                                }
                                                if (done) {
                                                    addLog("下载完成 **")
                                                    Thread{ upload() }.start()
                                                }
                                            }
                                        },
                                        { runOnUiThread { addLog("down write error!!") } })
                            }.start()
                        },
                        { addLog("down失败$it") },
                        { addLog("down start") },
                        { addLog("down end") }
                )
    }

    private fun upload() {
        val f = File(p1)
        var p = 0
        val requestFile = RequestBody.create(MediaType.parse("image/jpg"), f)
        val body1 = UploadRequestBody(requestFile) { progress, total ->
            (progress * 100 / total).toInt().also {
                if (it != p) {
                    p = it; runOnUiThread { addLog("up $p") }
                }
            }
        }
        val body = MultipartBody.Part.createFormData("file", f.name, body1)
        val map = hashMapOf<String, RequestBody>()
        map["key"] = RequestBody.create(MediaType.parse("text/plain"), "value")
        retrofit.upload(map, body)
                .enqueue2(this@RetrofitAc,
                        { runOnUiThread { addLog(it.body()?.string()) } },
                        { runOnUiThread { addLog("upload 失败$it") } },
                        { runOnUiThread { addLog("upload start") } },
                        { runOnUiThread { addLog("upload end") } }
                )
    }

    interface DemoService {

        @Headers("logHeader: true")
        @GET("todayOnhistory/queryEvent.php")
        fun get(@QueryMap params: HashMap<String, String>): Call<ResponseBody>

        @Headers("logHeader: true")
        @FormUrlEncoded
        @POST("toutiao/index")
        fun post(@Field("key") key: String, @Field("type") type: String): Call<ResponseBody>

        @Streaming
        @Headers("LogEnable: false")
        @GET
        fun download(@Url url: String): Call<ResponseBody>

        @Streaming
        @Headers("LogEnable: false")
        @Multipart
        @POST("https://imgkr.com/api/files/upload")
        fun upload(@PartMap body:@JvmSuppressWildcards Map<String, RequestBody>, @Part file: MultipartBody.Part): Call<ResponseBody>

    }
}