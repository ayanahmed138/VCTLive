package com.example.vctlive.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * In-memory cache of downloaded team logo bitmaps, keyed by URL.
 * Shared by TeamsIconBuilder (small collapsed icon) and MatchCardImageBuilder
 * (the big expanded card), so a team's logo is only ever downloaded once per
 * app process, no matter how many times it shows up across polls.
 */
object LogoCache {

    private val cache = mutableMapOf<String, Bitmap?>()

    suspend fun get(url: String?): Bitmap? {
        if (url.isNullOrBlank()) return null

        if (cache.containsKey(url)) return cache[url]

        val bitmap = withContext(Dispatchers.IO) {
            try {
                URL(url).openStream().use { BitmapFactory.decodeStream(it) }
            } catch (e: Exception) {
                null
            }
        }

        cache[url] = bitmap
        return bitmap
    }
}
