package com.example.vctlive.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vctlive.R

object NotificationHelper {

    const val CHANNEL_ID = "live_matches"

    fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Live Matches",
                // Must not be IMPORTANCE_MIN — that's one of the Live Update requirements.
                // LOW keeps it silent on every 30s update; onlyAlertOnce() backs that up.
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Live Valorant Match Updates"
            }

            val manager =
                context.getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds a Live Update (promoted ongoing) notification showing a real match.
     *
     * This can't use a custom RemoteViews layout — that's one of the platform's
     * requirements for the notification to be eligible for the status-bar chip /
     * lock-screen pill treatment. Android draws it from the standard fields below.
     */
    fun buildLiveUpdateNotification(
        context: Context,
        title: String,
        score: String,
        detail: String,
        event: String,
        // Composite "logo vs logo" image — shows in the expanded card
        // (shade / lock screen / AOD), NOT in the tiny status-bar chip itself.
        // The chip is always just the small icon + short text; that part
        // is fixed by the platform, not something an app can change.
        largeIcon: Bitmap? = null
    ): Notification {

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(score)
            .setOngoing(true)               // required: must be an ongoing notification
            .setOnlyAlertOnce(true)         // don't buzz/sound on every poll, just update quietly
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon)
        }

        // IMPORTANT: BigPictureStyle is NOT on Android 16's list of promotable styles
        // on any shipping device (only: no style, BigTextStyle, CallStyle, ProgressStyle).
        // Using it silently disqualifies the notification from the Live Update chip —
        // that's a confirmed platform limitation, not a bug in this code. Stick to
        // BigTextStyle here; the logos live in largeIcon instead (smaller, but it
        // keeps the chip working).
        val bigText = listOf(detail, event).filter { it.isNotBlank() }.joinToString("\n")

        if (bigText.isNotBlank()) {
            builder.setStyle(
                NotificationCompat.BigTextStyle().bigText(bigText)
            )
        }

        // Ask the system to promote this to a Live Update (Android 16+).
        // On older Android versions this call is a no-op — you just get a normal
        // ongoing notification instead of the status-bar chip / pill.
        builder.setRequestPromotedOngoing(true)

        // Text shown in the status bar chip itself, e.g. "5-7"
        builder.setShortCriticalText(score)

        return builder.build()
    }
}
