package com.zwping.mobile_quicker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zwping.mobile_quicker.databinding.AcSplashBinding
import java.text.SimpleDateFormat
import java.util.*


class AcSplash : AppCompatActivity() {

    private val vb by lazy { AcSplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        vb.btnDev.setOnClickListener { startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)) }
        vb.btnAcc.setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        vb.btnDebug.setOnClickListener {
            startActivity(Intent(if (Utils.isAdbEnable(this)) Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS else  Settings.ACTION_DEVICE_INFO_SETTINGS))
        }
        vb.btnProcess.setOnClickListener { startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)) }
    }

    private fun showToast(msg: Any?) { Toast.makeText(this, "$msg", Toast.LENGTH_SHORT).show() }

    override fun onResume() {
        super.onResume()
        Utils.isAccessibilitySettingsOn(this, AutoService::class.java).also {
            val adb = Utils.isAdbEnable(this)
            // vb.btnDebug.text = "开发者模式(${if (adb) "已打开" else "已关闭"})"
            vb.btnDev.visibility = if (adb) View.GONE else View.VISIBLE
            vb.btnAcc.text = "无障碍服务(${if (it) "已打开" else "已关闭"})"
            if (it) { return@also }
            AlertDialog.Builder(this).apply {
                setTitle("需要手动开启辅助服务")
                setNegativeButton("确认") { _, _ ->
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }
            }.create().show()
//            if (!adb) {
//                AlertDialog.Builder(this).apply {
//                    setTitle("请手动开启开发者模式")
//                    setNegativeButton("确认") { _, _ ->
//                        startActivity(Intent(Settings.ACTION_SETTINGS))
//                    }
//                }.create().show()
//                return
//            }
        }
    }

}