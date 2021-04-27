package com.zwping.jetpack.ktxs

import android.os.Build
import android.webkit.*

/**
 *
 * zwping @ 1/15/21
 */
inline fun WebView.initSetting() : WebSettings =
    settings.apply {
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        loadWithOverviewMode = true
        useWideViewPort = true
        //允许js代码
        javaScriptEnabled = true
        //允许SessionStorage/LocalStorage存储
        domStorageEnabled = true
        //禁用放缩
        displayZoomControls = false
        builtInZoomControls = false
        //禁用文字缩放
        textZoom = 100
        //10M缓存，api 18后，系统自动管理。
        setAppCacheMaxSize(10 * 1024 * 1024)
        //允许缓存，设置缓存位置
        setAppCacheEnabled(true)
        setAppCachePath(context.getDir("appcache", 0).path)
        //允许WebView使用File协议
        allowFileAccess = true
        //不保存密码
        savePassword = false
        //设置UA
        // setUserAgentString(getUserAgentString() + " kaolaApp/" + AppUtils.getVersionName())
        //移除部分系统JavaScript接口
        // KaolaWebViewSecurity.removeJavascriptInterfaces(webView)
        //自动加载图片
        loadsImagesAutomatically = true
    }

inline fun WebView.initClient(lis: (vc: WebViewClient, cc: WebChromeClient)->Unit, crossinline pageTitleLis:(String?)->Unit = {}) {
    val webChromeClient = object: WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            pageTitleLis.invoke(title)
        }
    }
    val webViewClient = object: WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) view.loadUrl(request.url.toString())
            else view.loadUrl(request.toString())
            return true
        }
    }
    setWebChromeClient(webChromeClient)
    setWebViewClient(webViewClient)
    lis.invoke(webViewClient, webChromeClient)
}

inline fun WebView.goBack2(): Boolean = if (canGoBack()) { goBack(); true} else false