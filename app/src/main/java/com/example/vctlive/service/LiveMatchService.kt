package com.example.vctlive.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.example.vctlive.data.datastore.UserPreferences
import com.example.vctlive.network.RetrofitInstance
import com.example.vctlive.notification.NotificationHelper
import com.example.vctlive.ui.util.toLogoUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Foreground service that keeps one followed live match's Live Update notification
 * up to date. Started from FollowingScreen whenever there's something followed;
 * stops itself once nothing followed is live anymore.
 */
class LiveMatchService : Service() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var preferences: UserPreferences

    companion object {
        private const val POLL_INTERVAL_MS = 30_000L
        const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        preferences = UserPreferences(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // A foreground service must show a notification immediately.
        // Real data replaces this placeholder within one poll cycle (~30s, sooner on start).
        startForeground(
            NOTIFICATION_ID,
            NotificationHelper.buildLiveUpdateNotification(
                context = this,
                title = "VCT Live",
                score = "Loading…",
                detail = "",
                event = ""
            )
        )

        scope.launch { pollLoop() }

        return START_STICKY
    }

    private suspend fun pollLoop() {
        while (true) {
            try {
                val followed = preferences.followedMatches.first()

                if (followed.isEmpty()) {
                    stopSelf()
                    return
                }

                val liveMatches = RetrofitInstance.api.getLiveMatches()
                val match = liveMatches.firstOrNull { it.matchId in followed }

                if (match == null) {
                    // Followed match isn't live right now (hasn't started / already finished).
                    // Stop for now — FollowingScreen restarts the service once something IS live.
                    stopSelf()
                    return
                }

                val smallIcon = TeamsIconBuilder.build(
                    match.team1, match.team1Logo.toLogoUrl(),
                    match.team2, match.team2Logo.toLogoUrl()
                )

                // Chip / collapsed row: the fast-changing round score for the current map.
                // Body (expanded): the slower series score (maps won) + which map this is.
                val mapLabel = if (match.mapNumber > 0) "Map ${match.mapNumber}: ${match.currentMap}"
                                else match.currentMap

                val notification = NotificationHelper.buildLiveUpdateNotification(
                    context = this@LiveMatchService,
                    title = "${match.team1} vs ${match.team2}",
                    score = match.currentMapScore,
                    detail = "Series ${match.seriesScore}  •  $mapLabel",
                    event = match.event,
                    largeIcon = smallIcon
                )

                NotificationManagerCompat.from(this@LiveMatchService)
                    .notify(NOTIFICATION_ID, notification)

            } catch (e: Exception) {
                // Network hiccup — try again next cycle rather than crashing the service.
            }

            delay(POLL_INTERVAL_MS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
