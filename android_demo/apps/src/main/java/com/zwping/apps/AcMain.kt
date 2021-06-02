package com.zwping.apps

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import com.bumptech.glide.Glide
import com.zwping.apps.databinding.AcMainBinding
import com.zwping.apps.databinding.ItemAppsBinding
import com.zwping.plibx.*
import com.zwping.plibx.IJson.Companion.optJSONArrayOrNull
import com.zwping.plibx.Requests.enqueue2
import com.zwping.plibx.Requests.enqueueDown
import com.zwping.plibx.Requests.execute2
import com.zwping.plibx.Requests.isSuccessful2Safe
import com.zwping.plibx.Requests.toJSONObject
import kotlinx.coroutines.*
import okhttp3.*
import java.io.*
import java.net.URL
import java.text.SimpleDateFormat
import kotlin.random.Random

// 应用内测分发平台一键升级
class AcMain : AppCompatActivity() {

    private val user by lazy {
        hashMapOf(
                Pair("all", null),
                Pair("fy", listOf(
                        "com.feiyan.tikuhui", // 题库汇
                        "com.yikao.xianshangkao", // 线上考
                        "com.yikao.app", // 艺考生
                        // "com.yikao.putonghua", // 普通话考试通
                )),
        )
    }


    private val adp by lazy { Adapter() }

    private val vb by lazy { AcMainBinding.inflate(layoutInflater) }
    private fun getCtx(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        vb.srl.setOnRefreshListener { getData() }
        vb.rv.layoutManager = LinearLayoutManager(this)
        vb.rv.adapter = adp
    }


    override fun onResume() {
        super.onResume()
        vb.srl.isRefreshing = true
        getData()
    }

    private fun showToast(msg: String) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }

    private fun getData() {
        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()
            val list = async { Requests.get("https://api.bq04.com/apps", hashMapOf(Pair("api_token", "526553d43bd95c5da73ce649dc826158"))).execute2() }.await()
            if (!list.isSuccessful2Safe()) {
                vb.srl.isRefreshing = false
                showToast(list.msg)
                return@launch
            }
            val datas = mutableListOf<AppsBean>()
            list.responseStr?.toJSONObject()?.optJSONArray("items")?.optJSONArrayOrNull {
                datas.add(AppsBean(it?.optString("name") ?: "", it?.optString("bundle_id") ?: "",
                        firLTime = it?.optLong("updated_at"),
                        svcode = it?.optJSONObject("master_release")?.optInt("build"),
                        svname = it?.optJSONObject("master_release")?.optString("version"),
                        short = it?.optString("short"), download_domain = it?.optString("download_domain")))
            }
            datas.sortByDescending { it.firLTime }
            packageManager.getInstalledPackages(0)
                    .filter { mutableListOf<String>().apply { datas.forEach { add(it.pkg) } }.contains(it.packageName) }
                    .forEach { pm ->
                        datas.filter { it.pkg == pm.packageName }.forEach {
                            it.icon = pm.applicationInfo.loadIcon(packageManager)  // app icon (Drawable)
                            it.localLTime = packageManager.getPackageInfo(pm.packageName, 0).lastUpdateTime
                            it.lfircode = packageManager.getApplicationInfo(pm.packageName, PackageManager.GET_META_DATA).metaData?.get("fircode")?.toString()?.toInt() // meta
                            it.lvcode = pm.versionCode
                            it.lvname = pm.versionName
                        }
                    }
            val args = hashMapOf<AppsBean, Deferred<Requests.Response2>>() // 并发分配
            datas.forEach {
                args[it] = async { Requests.get("https://fir-download.fircli.cn/${it.short}", hashMapOf(Pair("referer", it.download_domain)))
                        .execute2() }
            }
            args.forEach {
                it.value.await().apply {
                    if (isSuccessful2Safe()) {
                        val icon = responseStr?.toJSONObject()?.optJSONObject("app")?.optString("icon_url")
                        val log = responseStr?.toJSONObject()?.optJSONObject("app")?.optJSONObject("releases")?.optJSONObject("master")?.optInt("changelog")
                        datas[datas.indexOf(it.key)].also {
                            it.sfircode = log
                            if (it.icon == null) it.icon = icon
                            it.hasUpdate = (it.lvcode != null && it.svcode ?: 0 > it.lvcode ?: 0) || (it.lvcode != null && it.sfircode ?: 0 > it.lfircode ?: 0)
                        }
                    }
                }
            }
            datas.sortByDescending { it.hasUpdate }
            println("总耗时: ${System.currentTimeMillis() - startTime}")
            adp.setData(datas)
            vb.rv.smoothScrollToPosition(0)
            vb.srl.isRefreshing = false
        }
    }

    private val dateFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss") }

    private fun downFir(bean: AppsBean, pb: View, onProgress: (progress: Int, curSize: Long, total: Long, fileName: String) -> Unit) {
        lifecycleScope.launch {
            val detail = async {
                Requests.get("https://fir-download.fircli.cn/${bean.short}", hashMapOf(Pair("referer", bean.download_domain)))
                        .execute2() }.await()
            if (!detail.isSuccessful2Safe()) {
                showToast(detail.msg); return@launch
            }
            val args = Array(4){""}
            detail.responseStr!!.toJSONObject()?.optJSONObject("app")?.also {
                args[0] = it.optString("id")
                args[1] = it.optString("short")
                args[2] = it.optString("token")
                args[3] = it.optJSONObject("releases")?.optJSONObject("master")?.optString("id") ?: ""
            }
            Requests.get("https://fir-download.fircli.cn/apps/${args[0]}/" +
                    "install?short=${args[1]}&download_token=${args[2]}&release_id=${args[3]}")
                    .enqueueDown(
                            this@AcMain, filesDir.absolutePath,
                            { call, response, filePath ->
                                println(filePath);
                                install(getCtx(), filePath);
                            },
                            { call, msg -> showToast(msg) },
                            onProgress = onProgress,
                            name = "${bean.name}.apk",
                            {
                                vb.srl.isEnabled = false
                                pb.visibility = View.VISIBLE
                                ObjectAnimator.ofFloat(pb, "translationY", 2F.dp2px().toFloat(), 0F).apply {
                                    duration = 500; start()
                                }
                            },
                            {
                                vb.srl.isEnabled = true
                                ObjectAnimator.ofFloat(pb, "translationY", 0F, 2F.dp2px().toFloat()).apply {
                                    duration = 300; start()
                                }
                            }
                    )
        }
    }

    /**
     * context 上下文对象
     * filePath apk路径
     */
    private fun install(context: Context, filePath: String) {
        val apkFile = File(filePath)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri: Uri = FileProvider.getUriForFile(
                    context, context.packageName + ".file_provider", apkFile)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }


    data class AppsBean(var name: String, var pkg: String,
                        var downIng: Boolean = false, var icon: Any? = null,
                        var localLTime: Long? = null, var firLTime: Long? = null,
                        var lfircode: Int? = null, var sfircode: Int? = null,
                        var lvcode: Int? = null, var svcode: Int? = null,
                        var lvname: String? = null, var svname: String? = null,
                        var hasUpdate: Boolean = false,
                        var short: String? = "", var download_domain: String? = "")

    inner class Adapter : BaseAdapter<AppsBean>() {

        init {
            diffCallback = object: DiffCallback<AppsBean>() {
                override fun areItemsTheSame(od: AppsBean, nd: AppsBean): Boolean {
                    return od.name == nd.name && od.lvcode == nd.lvcode && od.hasUpdate == nd.hasUpdate
                            && od.localLTime == nd.localLTime && od.firLTime == nd.firLTime
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<AppsBean, ItemAppsBinding> {
            return BaseVH<AppsBean, ItemAppsBinding>(
                    ItemAppsBinding.inflate(parent.getLayoutInflater(), parent, false),
                    { vb, entity ->
                        vb.tvTop.visibility = View.GONE
                        vb.tvUninstall.visibility = if (entity.lvcode != null) View.VISIBLE else View.GONE
                        vb.btnInstall.text = if (entity.hasUpdate) "更新" else "下载"
                        vb.btnInstall.setTextColor(if (entity.hasUpdate) Color.GREEN else Color.GRAY)
                        vb.tvName.text = entity.name
                        vb.tvPkgName.text = entity.pkg
                        vb.pb.visibility = if (entity.downIng) View.VISIBLE else View.GONE
                        vb.tvDesc.text =
                                "local  : ${entity.localLTime?.let { dateFormat.format(it) } ?: ""} ${entity.lvname ?: ""} ${entity.lfircode?.let { "f$it" } ?: ""}" +
                                        "\nserver: ${entity.firLTime?.let { dateFormat.format(it * 1000) } ?: ""} ${entity.svname ?: ""} ${entity.sfircode?.let { "f$it" } ?: ""}"
                        entity.icon?.also { Glide.with(vb.ivIcon).load(it).into(vb.ivIcon) }
                    }).apply {
                        vb.pb.progressDrawable =
                                LayerDrawable(arrayOf(
                                        GradientDrawable().create { setColor(Color.LTGRAY) },
                                        ClipDrawable(GradientDrawable().create { setColor(Color.GREEN) }, Gravity.LEFT, ClipDrawable.HORIZONTAL),
                                )).apply {
                                    setId(0, android.R.id.background); setId(1, android.R.id.progress)
                                }
                        vb.lyRoot.setOnClickListener { }
                        vb.tvTop.setOnClickListener { }
                        vb.btnInstall.setOnClickListener {
                            println(entity)
                            if (entity?.lvcode ?: 0 > entity?.svcode ?: 0) {
                                AlertDialog.Builder(getCtx()).setTitle("本地版本高于内测平台版本").setNegativeButton("这就去重新确认"){_,_ ->}.show()
                                return@setOnClickListener
                            }
                            if (entity?.lvcode != null && entity?.hasUpdate == false) {
                                AlertDialog.Builder(getCtx()).setTitle("重新安装??").setNegativeButton("确定")
                                {_, _ ->
                                    downFir(entity!!, vb.pb) { progress, _, _, _ -> vb.pb.progress = progress }
                                }.show()
                                return@setOnClickListener
                            }
                            downFir(entity!!, vb.pb) { progress, _, _, _ -> vb.pb.progress = progress }
                        }
                        vb.tvUninstall.setOnClickListener {
                            startActivity(
                                    Intent().apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                                        data = Uri.fromParts("package", entity?.pkg ?: "", null)
                                    })
                        }
            }
        }

    }

    private fun upFir() {
        val path = "${filesDir.absolutePath}/123.apk"
        lifecycleScope.launch {
            val certify  = async {
                Requests.post("https://api.bq04.com/apps",
                        hashMapOf(Pair("type", "android"),
                                Pair("bundle_id", "com.feiyan.tikuhui"),
                                Pair("api_token", "526553d43bd95c5da73ce649dc826158")))
                        .execute2()
            }.await()
            if (!certify.isSuccessful2Safe()) {
                println(certify.msg); return@launch
            }
            val args = Array(3) {""}
            certify.responseStr!!.toJSONObject()?.optJSONObject("cert")?.optJSONObject("binary")?.apply {
                args[0] = optString("upload_url")
                args[1] = optString("key")
                args[2] = optString("token")
            }
            Requests.post(args[0], hashMapOf(Pair("key", args[1]), Pair("token", args[2]),
                    Pair("x:name", "题库汇"), Pair("x:version", "1.3.4"), Pair("x:build", 8), Pair("x:changelog", "")),
                    kwargs = {
                        initMultipartBodyIsFile("file", File(path))
                        { progress, curSize, total, fileName -> println("$progress -- $curSize -- $total $fileName") }
                    }
            ).enqueue2(this@AcMain,
                    { call, response -> println(response.body?.string()) },
                    { call, msg -> println(msg) })
        }
    }

}