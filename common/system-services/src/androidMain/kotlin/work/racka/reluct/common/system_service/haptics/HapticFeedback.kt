package work.racka.reluct.common.system_service.haptics

import android.content.Context
import android.os.*

actual class HapticFeedback(private val context: Context) {
    actual fun tick() {
        val durationMillis = 40L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrateOneShot(durationMillis, VibrationEffect.EFFECT_TICK)
        } else vibrateOneShot(durationMillis)
    }

    actual fun click() {
        val durationMillis = 60L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrateOneShot(durationMillis, VibrationEffect.EFFECT_CLICK)
        } else vibrateOneShot(durationMillis)
    }

    actual fun doubleClick() {
        val durationMillis = 100L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrateOneShot(durationMillis, VibrationEffect.EFFECT_DOUBLE_CLICK)
        } else vibrateOneShot(durationMillis)
    }

    actual fun heavyClick() {
        val durationMillis = 120L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrateOneShot(durationMillis, VibrationEffect.EFFECT_HEAVY_CLICK)
        } else vibrateOneShot(durationMillis)
    }

    actual fun customDuration(durationMillis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrateOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
        } else vibrateOneShot(durationMillis)
    }

    /**
     *  [amplitude] default = VibrationEffect.DEFAULT_AMPLITUDE which equals -1
     *  Amplitude is only used from [Build.VERSION_CODES.O] or API 26 and above
     *  **/
    private fun vibrateOneShot(durationMillis: Long, amplitude: Int = -1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrator =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrationEffect = VibrationEffect.createOneShot(durationMillis, amplitude)
            vibrator.vibrate(CombinedVibration.createParallel(vibrationEffect))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, amplitude))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMillis)
            }
        }
    }
}