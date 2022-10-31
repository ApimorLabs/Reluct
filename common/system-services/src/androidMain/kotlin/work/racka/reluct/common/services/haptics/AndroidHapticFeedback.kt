package work.racka.reluct.common.services.haptics

import android.content.Context
import android.os.*

internal class AndroidHapticFeedback(private val context: Context) : HapticFeedback {
    override fun tick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
            vibrate(DEFAULT_TICK_DURATION, vibrationEffect)
        } else {
            vibrate(DEFAULT_TICK_DURATION)
        }
    }

    override fun click() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrate(DEFAULT_CLICK_DURATION, vibrationEffect)
        } else {
            vibrate(DEFAULT_CLICK_DURATION)
        }
    }

    override fun doubleClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect
                .createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
            vibrate(DEFAULT_DOUBLE_CLICK_DURATION, vibrationEffect)
        } else {
            vibrate(DEFAULT_DOUBLE_CLICK_DURATION)
        }
    }

    override fun heavyClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect
                .createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
            vibrate(DEFAULT_LONG_VIBRATE_DURATION, vibrationEffect)
        } else {
            vibrate(DEFAULT_LONG_VIBRATE_DURATION)
        }
    }

    override fun cascadeFall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.startComposition()
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_RISE, 1f)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_FALL, 1f, QUICK_FALL_DELAY)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1f)
                .compose()
            vibrate(DEFAULT_LONG_VIBRATE_DURATION, vibrationEffect)
        } else {
            vibrate(DEFAULT_LONG_VIBRATE_DURATION)
        }
    }

    override fun spinAndFall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.startComposition()
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 1f)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_SPIN, 1f, 10)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_FALL, 1f)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1f)
                .compose()
            vibrate(DEFAULT_LONG_VIBRATE_DURATION, vibrationEffect)
        } else {
            vibrate(DEFAULT_LONG_VIBRATE_DURATION)
        }
    }

    override fun customDuration(durationMillis: Long) {
        vibrate(durationMillis)
    }

    /**
     *  [vibrationEffect] is only used from [Build.VERSION_CODES.O] or API 26 and above
     *  Some effects are also limited to Android 12+ or [Build.VERSION_CODES.S]
     *  It is set to null by default.
     *  If a [VibrationEffect] is not provided then [VibrationEffect.createOneShot] will be used
     *  with provided [durationMillis] and [VibrationEffect.DEFAULT_AMPLITUDE]
     *
     *  Below Android 8 (less than [Build.VERSION_CODES.O]), only the duration will be used.
     *  **/
    private fun vibrate(durationMillis: Long, vibrationEffect: VibrationEffect? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrator =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            if (vibrationEffect == null) {
                val effect = VibrationEffect
                    .createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(CombinedVibration.createParallel(effect))
            } else {
                vibrator.vibrate(CombinedVibration.createParallel(vibrationEffect))
            }
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrationEffect == null) {
                val effect = VibrationEffect
                    .createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            } else {
                vibrator.vibrate(vibrationEffect)
            }
        }
    }

    companion object {
        const val QUICK_FALL_DELAY = 20
        const val DEFAULT_TICK_DURATION = 20L
        const val DEFAULT_CLICK_DURATION = 40L
        const val DEFAULT_DOUBLE_CLICK_DURATION = 100L
        const val DEFAULT_LONG_VIBRATE_DURATION = 120L
    }
}
