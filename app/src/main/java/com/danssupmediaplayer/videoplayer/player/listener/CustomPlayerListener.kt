package com.danssupmediaplayer.videoplayer.player.listener

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.exoplayer.ExoPlayer

interface CustomPlayerListener {
    fun getPlayer(context: Context): ExoPlayer
    fun setMediaURL(url: String)
    fun isPlaying(): Boolean
    fun isVolumeOn(): Boolean
    fun mute()
    fun unMute()
    fun play()
    fun pause()
    fun stop()
    fun duration(): Long
    fun currentPosition(): Long
    fun rewind(sec: Int): Long
    fun forward(sec: Int): Long
    fun seekTo(sec: Int)
    fun playbackSlow()
    fun playbackMedium()
    fun playbackFast()
    fun releasePlayer()
}