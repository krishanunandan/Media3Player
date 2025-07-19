package com.danssupmediaplayer.videoplayer.player.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.danssupmediaplayer.R
import java.io.FileNotFoundException


fun Activity.hideSystemUI(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, view).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun ImageView.setImage(url: String) {
    try {
        if (url.isNotEmpty()) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(5000)

            Glide.with(this.context)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).error(R.drawable.ic_pause)
                .into(this)
        }
    } catch (exception: FileNotFoundException) {
        Log.e("Exception V", "${exception.message}")
    }
}

fun ImageView.setImageFromVideo(url: String) {
    try {
        val cropOptions = RequestOptions()

        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    } catch (exception: FileNotFoundException) {
        Log.e("Exception V", "${exception.message}")
    }
}

fun Activity.showSystemUI(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.systemBars())
}


