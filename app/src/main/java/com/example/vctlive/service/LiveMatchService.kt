package com.example.vctlive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.vctlive.notification.NotificationHelper


class LiveMatchService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        startForeground(
            1001,
            NotificationHelper.buildNotification(
                this,
                "🔴 LIVE",
                "Sentinels 1 - 0 PRX",
                "Map 2 • Haven (13-11)",
                "Masters Toronto"
            )
        )
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}