package com.danssupmediaplayer.videoplayer.ui.viewmodel


import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.danssupmediaplayer.videoplayer.player.listener.PlaybackStateListener
import com.danssupmediaplayer.videoplayer.player.VideoPlayer
import com.danssupmediaplayer.videoplayer.player.utils.Util
import com.danssupmediaplayer.videoplayer.player.utils.Util.setDurationInMinute

class VideoPlayerViewModel : ViewModel() {

    var playbackStateListener: PlaybackStateListener? = null

    private val videoPlayer = VideoPlayer()

    fun getMediaPlayer() = videoPlayer

    fun setMediaURL(url: String) = videoPlayer.setMediaURL(url)

    fun isPlaying(): Boolean {
        return videoPlayer.isPlaying()
    }

    fun isVolumeOn(): Boolean = videoPlayer.isVolumeOn()

    fun mute() = videoPlayer.mute()

    fun unMute() = videoPlayer.unMute()

    fun play() = videoPlayer.play()

    fun pause() = videoPlayer.pause()

    fun stop() = videoPlayer.stop()

    fun release() = videoPlayer.releasePlayer()

    fun totalDuration(): Long = videoPlayer.duration()

    fun currentPosition(): Long = videoPlayer.currentPosition()

    fun seekTo(sec: Int) = videoPlayer.seekTo(sec)

    fun setPlaybackStateListener(exoPlayer: ExoPlayer) {

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        playbackStateListener?.stateIdle()
                    }

                    Player.STATE_BUFFERING -> {
                        playbackStateListener?.stateBuffering()
                    }

                    Player.STATE_ENDED -> {
                        playbackStateListener?.stateEnded()
                    }

                    Player.STATE_READY -> {
                        playbackStateListener?.stateReady()
                    }
                }
            }
        })
    }

    fun setCustomSeekbarListener(playerSeekBar: SeekBar) {
        playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    fun rewind(): Long = videoPlayer.rewind(10000)
    fun forward(): Long = videoPlayer.forward(10000)

    fun playbackSlow() = videoPlayer.playbackSlow()
    fun playbackMedium() = videoPlayer.playbackMedium()
    fun playbackFast() = videoPlayer.playbackFast()


}