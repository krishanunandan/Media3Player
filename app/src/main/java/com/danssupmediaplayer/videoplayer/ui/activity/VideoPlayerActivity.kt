package com.danssupmediaplayer.videoplayer.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import com.danssupmediaplayer.R
import com.danssupmediaplayer.databinding.ActivityVideoPlayerBinding
import com.danssupmediaplayer.videoplayer.player.listener.PlaybackStateListener
import com.danssupmediaplayer.videoplayer.ui.viewmodel.VideoPlayerViewModel
import com.danssupmediaplayer.videoplayer.player.utils.Constant.IS_PORTRAIT
import com.danssupmediaplayer.videoplayer.player.utils.Constant.VIDEO_SEEK_TO
import com.danssupmediaplayer.videoplayer.player.utils.Util
import com.danssupmediaplayer.videoplayer.player.utils.hideSystemUI
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class VideoPlayerActivity : AppCompatActivity(), View.OnClickListener, PlaybackStateListener {
    lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var videoPlayerViewModel: VideoPlayerViewModel
    private var seekTo: Int = 0
    private lateinit var exoPlayer: ExoPlayer
    private var executorService: ScheduledExecutorService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI(binding.root)
        init()
        initListener()

    }

    private fun init() {
        videoPlayerViewModel = ViewModelProvider(this)[VideoPlayerViewModel::class.java]
        videoPlayerViewModel.playbackStateListener = this
        exoPlayer = videoPlayerViewModel.getMediaPlayer().getPlayer(this)
        videoPlayerViewModel.setPlaybackStateListener(exoPlayer)
        binding.videoFullScreenPlayer.player = exoPlayer
        val url = "https://www.w3schools.com/tags/mov_bbb.mp4"
        videoPlayerViewModel.setMediaURL(url)




        binding.mediaPlayerSeekBarExo.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, p2: Boolean) {
                seekTo = progress
                binding.tvStartExo.text = Util.setDurationInMinute(progress)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                playPause()
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                videoPlayerViewModel.seekTo(seekTo)
                playPause()
            }

        })


    }

    override fun onRestart() {
        super.onRestart()
        videoPlayerViewModel.seekTo(VIDEO_SEEK_TO)
        videoPlayerViewModel.play()
    }

    private fun initListener() {
        binding.ivPlayPauseMain.setOnClickListener(this)
        binding.ivPlayPauseExo.setOnClickListener(this)
        binding.ivMinus.setOnClickListener(this)
        binding.ivPlus.setOnClickListener(this)
        binding.ivVolume.setOnClickListener(this)
        binding.tvSlow.setOnClickListener(this)
        binding.tvMedium.setOnClickListener(this)
        binding.tvFast.setOnClickListener(this)
        binding.ivRotate.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        videoPlayerViewModel.seekTo(VIDEO_SEEK_TO)
        videoPlayerViewModel.play()
    }


    override fun onStop() {
        super.onStop()
        seekTo = videoPlayerViewModel.currentPosition().toInt()
        videoPlayerViewModel.pause()
    }

    override fun onPause() {
        super.onPause()
        videoPlayerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerViewModel.release()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivPlayPauseMain -> {
                playPause()
            }

            binding.ivPlayPauseExo -> {
                playPause()
            }

            binding.ivMinus -> {
                val currentDuration = videoPlayerViewModel.rewind()
                binding.mediaPlayerSeekBarExo.progress = currentDuration.toInt()
            }

            binding.ivPlus -> {
                val currentDuration = videoPlayerViewModel.forward()
                binding.mediaPlayerSeekBarExo.progress = currentDuration.toInt()
            }

            binding.ivVolume -> {
                if (videoPlayerViewModel.isVolumeOn()) {
                    videoPlayerViewModel.mute()
                    binding.ivVolume.setImageResource(R.drawable.ic_volume_off)
                } else {
                    binding.ivVolume.setImageResource(R.drawable.ic_volume_on)
                    videoPlayerViewModel.unMute()
                }
            }

            binding.tvSlow -> {
                binding.tvSlow.setBackgroundResource(R.drawable.bg_button_radial_shape)
                binding.tvMedium.setBackgroundResource(R.drawable.radial_opacity)
                binding.tvFast.setBackgroundResource(R.drawable.radial_opacity)
                videoPlayerViewModel.playbackSlow()
            }

            binding.tvMedium -> {
                binding.tvSlow.setBackgroundResource(R.drawable.radial_opacity)
                binding.tvMedium.setBackgroundResource(R.drawable.bg_button_radial_shape)
                binding.tvFast.setBackgroundResource(R.drawable.radial_opacity)
                videoPlayerViewModel.playbackMedium()
            }

            binding.tvFast -> {
                binding.tvSlow.setBackgroundResource(R.drawable.radial_opacity)
                binding.tvMedium.setBackgroundResource(R.drawable.radial_opacity)
                binding.tvFast.setBackgroundResource(R.drawable.bg_button_radial_shape)
                videoPlayerViewModel.playbackFast()
            }

            binding.ivRotate -> {
                VIDEO_SEEK_TO = videoPlayerViewModel.currentPosition().toInt()
                requestedOrientation = if (IS_PORTRAIT) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                IS_PORTRAIT = !IS_PORTRAIT
            }
        }
    }

    private fun playPause() {
        if (videoPlayerViewModel.isPlaying()) {
            videoPlayerViewModel.pause()
            binding.ivPlayPauseMain.setImageResource(R.drawable.ic_play)
            binding.ivPlayPauseExo.setImageResource(R.drawable.ic_play)
        } else {
            videoPlayerViewModel.play()
            binding.ivPlayPauseMain.setImageResource(R.drawable.ic_pause)
            binding.ivPlayPauseExo.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun startTimer() {
        executorService = Executors.newSingleThreadScheduledExecutor()
        try {
            executorService!!.scheduleAtFixedRate(
                {
                    runOnUiThread {
                        Log.e("CurrentPosition - ", "" + videoPlayerViewModel.currentPosition())
                        binding.tvStartExo.text =
                            Util.setDurationInMinute(videoPlayerViewModel.currentPosition().toInt())
                        binding.mediaPlayerSeekBarExo.progress =
                            videoPlayerViewModel.currentPosition().toInt()
                    }

                }, 0, 1, TimeUnit.SECONDS
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CurrentPositionEx - ", "" + e.printStackTrace())
            executorService!!.shutdown()
        }
    }


    private fun stopTimer() {
        if (executorService != null) {
            executorService!!.shutdown()
        }
    }

    override fun stateIdle() {
        binding.spinnerVideoDetails.visibility = View.VISIBLE
        stopTimer()
    }

    override fun stateBuffering() {
        binding.spinnerVideoDetails.visibility = View.VISIBLE
        stopTimer()
    }

    override fun stateEnded() {
        binding.spinnerVideoDetails.visibility = View.GONE
        binding.mediaPlayerSeekBarExo.progress = 0
        videoPlayerViewModel.seekTo(0)
        binding.tvStartExo.text = "00:00"
    }

    override fun stateReady() {
        binding.spinnerVideoDetails.visibility = View.GONE
        binding.mediaPlayerSeekBarExo.max = videoPlayerViewModel.totalDuration().toInt()
        binding.tvEndExo.text =
            Util.setDurationInMinute(videoPlayerViewModel.totalDuration().toInt())
        startTimer()
    }
}




