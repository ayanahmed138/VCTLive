package com.example.vctlive.service

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader

/**
 * Draws the full "match card" shown when the notification is expanded
 * (shade / lock screen), via BigPictureStyle — a standard notification style,
 * not a custom RemoteViews layout, so it's still allowed on a Live Update.
 *
 * Logos are the focal point: big circles either side, score centered between
 * them, map/series line underneath, event name up top.
 */
object MatchCardImageBuilder {

    private const val WIDTH = 900
    private const val HEIGHT = 450
    private const val LOGO_SIZE = 190
    private const val LOGO_TOP = 90f
    private const val SIDE_MARGIN = 90f

    private const val BG_COLOR = "#12151A"
    private const val ACCENT_COLOR = "#E53935"   // matches the app's red buttons
    private const val MUTED_COLOR = "#9AA0A6"

    fun build(
        team1Name: String,
        team1Logo: Bitmap?,
        team2Name: String,
        team2Logo: Bitmap?,
        score: String,
        mapLine: String,
        event: String
    ): Bitmap {

        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background card
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor(BG_COLOR) }
        canvas.drawRoundRect(RectF(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat()), 40f, 40f, bgPaint)

        // Thin accent bar along the top — a bit of identity without needing a photo
        val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor(ACCENT_COLOR) }
        canvas.drawRoundRect(RectF(0f, 0f, WIDTH.toFloat(), 10f), 6f, 6f, accentPaint)

        // Event name, top center
        val eventPaint = textPaint(color = MUTED_COLOR, size = 30f, bold = false)
        canvas.drawText(event, WIDTH / 2f, 64f, eventPaint)

        // Two team logos
        drawLogo(canvas, team1Logo, team1Name, x = SIDE_MARGIN)
        drawLogo(canvas, team2Logo, team2Name, x = WIDTH - SIDE_MARGIN - LOGO_SIZE)

        // Team names, under each logo
        val namePaint = textPaint(color = "#FFFFFF", size = 32f, bold = true)
        canvas.drawText(team1Name, SIDE_MARGIN + LOGO_SIZE / 2f, LOGO_TOP + LOGO_SIZE + 46f, namePaint)
        canvas.drawText(team2Name, WIDTH - SIDE_MARGIN - LOGO_SIZE / 2f, LOGO_TOP + LOGO_SIZE + 46f, namePaint)

        // Score, dead center, biggest element on the card
        val scorePaint = textPaint(color = "#FFFFFF", size = 96f, bold = true)
        val scoreY = LOGO_TOP + LOGO_SIZE / 2f - (scorePaint.descent() + scorePaint.ascent()) / 2f
        canvas.drawText(score, WIDTH / 2f, scoreY, scorePaint)

        // Map / series line, just under the score
        val mapPaint = textPaint(color = MUTED_COLOR, size = 30f, bold = false)
        canvas.drawText(mapLine, WIDTH / 2f, scoreY + 56f, mapPaint)

        return bitmap
    }

    private fun drawLogo(canvas: Canvas, logo: Bitmap?, name: String, x: Float) {

        val rect = RectF(x, LOGO_TOP, x + LOGO_SIZE, LOGO_TOP + LOGO_SIZE)

        if (logo != null) {
            val scaled = Bitmap.createScaledBitmap(logo, LOGO_SIZE, LOGO_SIZE, true)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }
            canvas.drawOval(rect, paint)
        } else {
            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#2A2E35") }
            canvas.drawOval(rect, bgPaint)

            val textPaint = textPaint(color = "#FFFFFF", size = LOGO_SIZE * 0.42f, bold = true)
            val ty = LOGO_TOP + LOGO_SIZE / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
            canvas.drawText(name.take(1).uppercase(), x + LOGO_SIZE / 2f, ty, textPaint)
        }
    }

    private fun textPaint(color: String, size: Float, bold: Boolean) =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.parseColor(color)
            textSize = size
            textAlign = Paint.Align.CENTER
            isFakeBoldText = bold
        }
}
