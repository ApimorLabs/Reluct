package work.racka.reluct.android.compose.components.util

import androidx.compose.ui.graphics.Color
import work.racka.reluct.common.model.domain.core.Icon
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.palette.graphics.Palette
import co.touchlab.kermit.Logger

actual fun Icon.extractColor(defaultColor: Color): Color {
    val bitmap = this.icon.getBitmap()
    return bitmap?.let { icon ->
        val palette = Palette.from(icon).generate()
        palette.getDominantColor(defaultColor.toLegacyInt())
    }?.let { Color(it) } ?: defaultColor
}

fun Drawable.getBitmap(): Bitmap? =
    try {
        val bitmap: Bitmap = Bitmap.createBitmap(
            this.intrinsicWidth,
            this.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        bitmap
    } catch (e: OutOfMemoryError) {
        // Handle the error
        Logger.d("Bitmap Convert Error: ${e.message}")
        null
    }
