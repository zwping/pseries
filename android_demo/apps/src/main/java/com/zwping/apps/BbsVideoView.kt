package com.zwping.apps

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.*
import com.zwping.apps.databinding.VideoViewBbsBinding
import com.zwping.plibx.setColors
import com.zwping.plibx.setOnDebounceClickListener
import java.text.SimpleDateFormat

/**
 *
android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode"
android:screenOrientation="portrait"
 * zwping @ 2021/7/23
 */
class BbsVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val vb by lazy { VideoViewBbsBinding.inflate(LayoutInflater.from(context), this, true) }
    val videoPlayer by lazy { GSY(context).apply {
        vb.root.addView(this, 0)
        titleTextView.visibility = GONE
        backButton.visibility = GONE
        fullscreenButton.visibility = GONE
        startButton.alpha = 0F
        findViewById<View>(R.id.layout_top).alpha = 0F
        findViewById<View>(R.id.loading).alpha = 0F
        findViewById<View>(R.id.layout_bottom).alpha = 0F
        findViewById<View>(R.id.bottom_progressbar).alpha = 0F
        thumbImageView = AppCompatImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
    } }

    private var useClick = false // 用户点击/自动播放，控制权上放至gsy

    init {
        PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
        vb.bbsClickMask.setOnClickListener {
            if (!videoPlayer.isInPlayingState) return@setOnClickListener
            videoPlayer.startWindowFullscreen(context, true, true)
        }

        vb.bbsStart.setOnDebounceClickListener {
            println("${videoPlayer.currentState} --")
            when(videoPlayer.currentState) {
                CURRENT_STATE_ERROR                     // 错误状态
                -> {
                    videoPlayer.startPlayLogic()
                    // return@setOnDebounceClickListener
                }
                CURRENT_STATE_PAUSE                     // 暂停
                -> {
                    videoPlayer.continuePlay()
                }
                CURRENT_STATE_NORMAL,                   // 正常
                CURRENT_STATE_AUTO_COMPLETE             // 自动播放结束
                -> {
                    videoPlayer.seekOnStart = videoPlayer.currentPositionWhenPlaying.toLong()
                    videoPlayer.startPlayLogic()
                }
                CURRENT_STATE_PLAYING_BUFFERING_START,  // 开始缓冲
                CURRENT_STATE_PREPAREING,               // 准备中
                CURRENT_STATE_PLAYING,                  // 播放中
                -> {}
                else -> { Toast.makeText(context, "未知状态", Toast.LENGTH_SHORT).show(); return@setOnDebounceClickListener }
            }
            useClick = true
            vb.bbsStart.visibility = View.GONE
        }

        vb.bbsProgressBar.setColors(Color.WHITE, Color.parseColor("#0a84ff"), Color.parseColor("#1a999999"))
        vb.bbsProgressBar.alpha = 0F
    }

    private val timeFormat by lazy { SimpleDateFormat("mm:ss") }

    private val progress = GSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
        if (vb.bbsStart.visibility == VISIBLE) vb.bbsStart.visibility = GONE
        if (vb.bbsLoading.visibility == VISIBLE) vb.bbsStart.visibility = GONE
        if (vb.bbsProgressBar.alpha == 0F) vb.bbsProgressBar.animate().alpha(1F)
        vb.bbsProgressBar.progress = progress
        vb.bbsProgressBar.secondaryProgress = secProgress
        if (currentPosition != 0) vb.bbsDuration.text = "${timeFormat.format(duration - currentPosition)}"
        println("$currentPosition --- $duration")
    }
    private val callback = object: GSYSampleCallBack() {
        override fun onStartPrepared(url: String?, vararg objects: Any?) {  // 开始加载
            vb.bbsLoading.visibility = VISIBLE
        }
        override fun onPrepared(url: String?, vararg objects: Any?) {       // 加载成功
            // orientationUtils.setEnable(true);
            // isPlay = true
            vb.bbsLoading.visibility = GONE
            if (useClick && !videoPlayer.isInPlayingState) {
                vb.bbsStart.visibility = GONE
                videoPlayer.seekOnStart = videoPlayer.currentPositionWhenPlaying.toLong()
                videoPlayer.startPlayLogic()
            }
        }
        override fun onPlayError(url: String?, vararg objects: Any?) {      // 播放错误
            vb.bbsStart.setImageResource(R.drawable.video_error_normal)
            vb.bbsStart.visibility = VISIBLE
            vb.bbsLoading.visibility = GONE
        }
        override fun onAutoComplete(url: String?, vararg objects: Any?) {   // 播放完了
            vb.bbsStart.setImageResource(R.mipmap.icon_replay)
            vb.bbsStart.visibility = VISIBLE
            vb.bbsLoading.visibility = GONE
            vb.bbsProgressBar.progress = 100
            vb.bbsDuration.text = "${timeFormat.format(videoPlayer.duration)}"
        }
        override fun onComplete(url: String?, vararg objects: Any?) {       // 非正常播放完了
            vb.bbsProgressBar.progress = 0
            vb.bbsDuration.text = "${timeFormat.format(videoPlayer.duration)}"
        }
        override fun onEnterFullscreen(url: String?, vararg objects: Any?) { // 进去全屏
            orientationUtils.isEnable = true
        }
        override fun onQuitFullscreen(url: String?, vararg objects: Any?) { // 退出全屏
            orientationUtils.isEnable = false
            orientationUtils.backToProtVideo()
            if (videoPlayer.currentState == CURRENT_STATE_PAUSE) {
                vb.bbsStart.visibility = VISIBLE
                vb.bbsStart.setImageResource(R.mipmap.icon_vplay)
            }
        }
    }
//    var isPlay = false
//    var isPause = false
    val orientationUtils by lazy { OrientationUtils(context as Activity?, videoPlayer).apply { isEnable = false } }
    val videoOption by lazy { GSYVideoOptionBuilder() }

    fun setDetails(videoPath: String?, coverPath: String?) {
         Glide.with(context).load(coverPath).into(videoPlayer.thumbImageView as AppCompatImageView)
        videoOption
            .setUrl(videoPath)
            .setIsTouchWiget(false)
            .setRotateViewAuto(false)
            // .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(true)
            .setReleaseWhenLossAudio(false)
            .setNeedLockFull(false)
            .setCacheWithPlay(false)
            .setVideoAllCallBack(callback)
            .setGSYVideoProgressListener(progress)
            .build(videoPlayer)
    }

    fun setList(tag: String, position: Int, videoPath: String?, coverPath: String?) {
        setDetails(videoPath, coverPath)
        videoPlayer.playTag = tag
        videoPlayer.playPosition = position
    }

    /*** 生命周期 ***/

    fun onBackPressed() : Boolean {
        // orientationUtils.backToProtVideo()
        return GSYVideoManager.backFromWindowFull(context)
    }

    fun onPause() {
        GSYVideoManager.onPause()
        // currentPlayer.onVideoPause()
//        isPause = true
    }

    fun onResume() { GSYVideoManager.onResume() }

//    fun onStop() {
        // if (isPlay) videoPlayer.currentPlayer.release()
        // orientationUtils.releaseListener()
//    }

    fun onDestroy() { GSYVideoManager.releaseAllVideos() }

    fun onListViewPosition(adp: RecyclerView.Adapter<*>) {
        val position = GSYVideoManager.instance().playPosition
        if (position < 0) return

    }

//    fun onConfigurationChanged2(newConfig: Configuration) {
//        val ac = context
//        if (ac is Activity && isPlay && !isPause) videoPlayer.onConfigurationChanged(ac, newConfig, orientationUtils)
//    }

    class GSY : StandardGSYVideoPlayer {

        constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

        fun continuePlay() {
            if (!mHadPlay && !mStartAfterPrepared) { startAfterPrepared() }
            try { gsyVideoManager.start()
            } catch (e: java.lang.Exception) { e.printStackTrace() }
            setStateAndUi(CURRENT_STATE_PLAYING)
        }
    }
}