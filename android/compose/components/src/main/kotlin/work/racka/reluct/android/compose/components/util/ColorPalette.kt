package work.racka.reluct.android.compose.components.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette

@ColorInt
fun Drawable.extractColor(@ColorInt defaultColor: Int = Color.GRAY): Int {
    val bitmap = this.getBitmap()
    return bitmap?.let { icon ->
        val palette = Palette.from(icon).generate()
        palette.getDominantColor(defaultColor)
    } ?: defaultColor
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
        null
    }
