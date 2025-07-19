package com.danssupmediaplayer.videoplayer.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.danssupmediaplayer.videoplayer.player.listener.CustomPlayerListener
import com.danssupmediaplayer.videoplayer.player.utils.CacheManager

class VideoPlayer : CustomPlayerListener {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context
    private var simpleCache: SimpleCache? = null


    @OptIn(UnstableApi::class)
    fun getCustomLoadControl(): LoadControl {
        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                10_000, // minBufferMs
                60_000, // maxBufferMs
                1500,   // bufferForPlaybackMs
                3000    // bufferForPlaybackAfterRebufferMs
            )
            .build()
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).setLoadControl(getCustomLoadControl()).build()

    }

    override fun getPlayer(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        return exoPlayer
    }

    @OptIn(UnstableApi::class)
    override fun setMediaURL(url: String) {
        try {
            if (simpleCache == null) {
                simpleCache= CacheManager.getSimpleCache(context)
            }

            // Create a cache-aware DataSource.Factory
            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(simpleCache!!)
                .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

            // Create a MediaItem
            val mediaItem = MediaItem.fromUri(url)

            // Use ProgressiveMediaSource for non-HLS/DASH
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(mediaItem)

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()


            // val mediaItem = MediaItem.fromUri(url)
            //exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun isPlaying(): Boolean {
        var isPlaying = false
        try {
            isPlaying = exoPlayer.isPlaying
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isPlaying
    }

    override fun isVolumeOn(): Boolean {
        var isVolumeOn = false
        try {
            val volume = exoPlayer.volume
            isVolumeOn = volume == 1f

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isVolumeOn
    }

    override fun mute() {
        try {
            exoPlayer.volume = 0.0f
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun unMute() {
        try {
            exoPlayer.volume = 1.0f
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun play() {
        try {
            exoPlayer.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun pause() {
        try {
            exoPlayer.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        try {
            exoPlayer.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun duration(): Long {
        var duration: Long = 0
        try {
            duration = exoPlayer.duration
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return duration
    }

    override fun currentPosition(): Long {
        var currentPosition: Long = 0
        try {
            currentPosition = exoPlayer.currentPosition
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentPosition
    }

    override fun rewind(sec: Int): Long {
        var currentDuration: Long = 0
        try {
            currentDuration = if (0 < exoPlayer.currentPosition - sec) {
                val n = exoPlayer.currentPosition - sec
                exoPlayer.seekTo(n)
                n
            } else {
                exoPlayer.seekTo(0)
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return currentDuration
    }

    override fun forward(sec: Int): Long {
        var currentDuration: Long = 0
        try {
            if (exoPlayer.duration > exoPlayer.currentPosition + sec) {
                val n = exoPlayer.currentPosition + sec
                exoPlayer.seekTo(n)
                currentDuration = n
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentDuration
    }

    override fun seekTo(sec: Int) {
        try {
            exoPlayer.seekTo(sec.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun playbackSlow() {
        try {
            exoPlayer.playbackParameters = PlaybackParameters(0.5f, 1f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun playbackMedium() {
        try {
            exoPlayer.playbackParameters = PlaybackParameters(1f, 1f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun playbackFast() {
        try {
            exoPlayer.playbackParameters = PlaybackParameters(2f, 1f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun releasePlayer() {
        try {
            exoPlayer.stop()
            exoPlayer.release()
            CacheManager.releaseCache()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
