package com.danssupmediaplayer.videoplayer.player.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

object CacheManager {
    private var simpleCache: SimpleCache? = null

    @OptIn(UnstableApi::class)
    fun getSimpleCache(context: Context): SimpleCache {
        if (simpleCache == null) {
            val cacheDir = File(context.cacheDir, "exo_cache")
            val evictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024) // 100MB
            val databaseProvider =  StandaloneDatabaseProvider(context)
            simpleCache = SimpleCache(cacheDir, evictor,databaseProvider)
        }
        return simpleCache!!
    }

    @OptIn(UnstableApi::class)
    fun releaseCache() {
        simpleCache?.release()
        simpleCache = null
    }
}