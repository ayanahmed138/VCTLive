package com.example.vctlive.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.vctlive.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object NotificationHelper {

    const val CHANNEL_ID = "live_matches"
    private const val LIVE_NOTIFICATION_ID = 1001

    fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Live Matches",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Live Valorant Match Updates"
            }

            val manager =
                context.getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }
    }

    fun buildNotification(
        context: Context,
        title: String,
        score: String,
        map: String,
        event: String
    ): Notification {

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(score)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$map\n$event")
            )
            .setOngoing(true)
            .build()
    }

    private suspend fun bitmapFromUrl(url: String): Bitmap =
        withContext(Dispatchers.IO) {

            URL(url).openStream().use {
                BitmapFactory.decodeStream(it)
            }

        }

    suspend fun showLiveNotification(
        context: Context,
        team1: String,
        team2: String,
        score: String,
        map: String,
        series: String,
        event: String
    ) {

        // Temporary placeholder logos
        // Replace these URLs with your API values later.
        val team1Bitmap = bitmapFromUrl(
            "https://owcdn.net/img/632be9976b8fe.png"
        )

        val team2Bitmap = bitmapFromUrl(
            "https://owcdn.net/img/632be9976b8fe.png"
        )

        //-------------------------
        // Collapsed Notification
        //-------------------------

        val collapsed = RemoteViews(
            context.packageName,
            R.layout.notification_collapsed
        )

        collapsed.setImageViewBitmap(
            R.id.team1Logo,
            team1Bitmap
        )

        collapsed.setImageViewBitmap(
            R.id.team2Logo,
            team2Bitmap
        )

        collapsed.setTextViewText(
            R.id.score,
            score
        )

        //-------------------------
        // Expanded Notification
        //-------------------------

        val expanded = RemoteViews(
            context.packageName,
            R.layout.notification_live
        )

        expanded.setImageViewBitmap(
            R.id.team1Logo,
            team1Bitmap
        )

        expanded.setImageViewBitmap(
            R.id.team2Logo,
            team2Bitmap
        )

        expanded.setTextViewText(
            R.id.team1Name,
            team1
        )

        expanded.setTextViewText(
            R.id.team2Name,
            team2
        )

        expanded.setTextViewText(
            R.id.score,
            score
        )

        expanded.setTextViewText(
            R.id.map,
            map
        )

        expanded.setTextViewText(
            R.id.series,
            series
        )

        expanded.setTextViewText(
            R.id.event,
            event
        )

        //-------------------------
        // Notification
        //-------------------------

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsed)
                .setCustomBigContentView(expanded)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        NotificationManagerCompat.from(context)
            .notify(
                LIVE_NOTIFICATION_ID,
                notification
            )
    }
}