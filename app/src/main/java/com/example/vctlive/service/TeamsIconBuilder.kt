package com.example.vctlive.service

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Builds the "team1 logo  vs  team2 logo" bitmap used as the notification's large icon.
 * Downloads are plain HTTP — no Coil here since this runs outside Compose, in the service.
 */
object TeamsIconBuilder {

    private const val CANVAS_SIZE = 256
    private const val LOGO_SIZE = 108
    private const val GAP = 24

    suspend fun build(
        team1Name: String,
        team1LogoUrl: String?,
        team2Name: String,
        team2LogoUrl: String?
    ): Bitmap = withContext(Dispatchers.IO) {

        val logo1 = LogoCache.get(team1LogoUrl) ?: placeholder(team1Name)
        val logo2 = LogoCache.get(team2LogoUrl) ?: placeholder(team2Name)

        val canvasBitmap = Bitmap.createBitmap(CANVAS_SIZE, CANVAS_SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(canvasBitmap)

        val totalWidth = LOGO_SIZE * 2 + GAP
        val startX = (CANVAS_SIZE - totalWidth) / 2
        val y = (CANVAS_SIZE - LOGO_SIZE) / 2

        canvas.drawBitmap(
            Bitmap.createScaledBitmap(logo1, LOGO_SIZE, LOGO_SIZE, true),
            startX.toFloat(), y.toFloat(), null
        )
        canvas.drawBitmap(
            Bitmap.createScaledBitmap(logo2, LOGO_SIZE, LOGO_SIZE, true),
            (startX + LOGO_SIZE + GAP).toFloat(), y.toFloat(), null
        )

        canvasBitmap
    }

    // Grey circle with the team's first letter — same idea as the in-app TeamLogo composable,
    // just drawn onto a plain Bitmap since notifications can't use Compose.
    private fun placeholder(name: String): Bitmap {
        val bitmap = Bitmap.createBitmap(LOGO_SIZE, LOGO_SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.DKGRAY
        canvas.drawCircle(LOGO_SIZE / 2f, LOGO_SIZE / 2f, LOGO_SIZE / 2f, paint)

        paint.color = Color.WHITE
        paint.textSize = LOGO_SIZE * 0.42f
        paint.textAlign = Paint.Align.CENTER
        val textY = LOGO_SIZE / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas.drawText(name.take(1).uppercase(), LOGO_SIZE / 2f, textY, paint)

        return bitmap
    }
}
