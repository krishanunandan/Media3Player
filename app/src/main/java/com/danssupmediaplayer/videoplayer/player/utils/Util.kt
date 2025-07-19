package com.danssupmediaplayer.videoplayer.player.utils


object  Util {

    fun setDurationInMinute(milliseconds: Int): String {
        var time = ""
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        time = if (minutes == 0) {
            "00:"
        } else if (minutes < 10) {
            "0$minutes:"
        } else {
            "$minutes:"
        }
        time += if (seconds == 0) {
            "00"
        } else if (seconds < 10) {
            "0$seconds"
        } else {
            seconds.toString()
        }
        return time

    }
}