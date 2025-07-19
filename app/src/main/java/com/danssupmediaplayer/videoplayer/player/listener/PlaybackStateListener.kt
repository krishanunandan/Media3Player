package com.danssupmediaplayer.videoplayer.player.listener

interface PlaybackStateListener {
    fun stateIdle()
    fun stateBuffering()
    fun stateEnded()
    fun stateReady()

}