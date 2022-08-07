package work.racka.reluct.common.system_service.haptics

import android.content.Context
import android.os.*

internal class AndroidHapticFeedback(private val context: Context) : HapticFeedback {
    override fun tick() {
        val durationMillis = 20L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
            vibrate(durationMillis, vibrationEffect)
        } else vibrate(durationMillis)
    }

    override fun click() {
        val durationMillis = 40L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrate(durationMillis, vibrationEffect)
        } else vibrate(durationMillis)
    }

    override fun doubleClick() {
        val durationMillis = 100L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect
                .createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
            vibrate(durationMillis, vibrationEffect)
        } else vibrate(durationMillis)
    }

    override fun heavyClick() {
        val durationMillis = 120L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect
                .createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
            vibrate(durationMillis, vibrationEffect)
        } else vibrate(durationMillis)
    }

    override fun cascadeFall() {
        val durationMillis = 120L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.startComposition()
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_RISE, 1f)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_FALL, 1f, 20)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1f)
                .compose()
            vibrate(durationMillis, vibrationEffect)
        } else vibrate(durationMillis)
    }

    override fun spinAndFall() {
        val durationMillis = 120L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.startComposition()
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 1f)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_SPIN, 1f, 10)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_FALL, 1f)
                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1f)
                .compose()
            vibrate(durationMillis, vibrationEffect)
        } else vibrate(durationMillis)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrationEffect == null) {
                    val effect = VibrationEffect
                        .createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(effect)
                } else vibrator.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMillis)
            }
        }
    }
}