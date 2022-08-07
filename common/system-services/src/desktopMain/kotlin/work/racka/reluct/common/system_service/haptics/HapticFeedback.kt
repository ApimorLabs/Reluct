package work.racka.reluct.common.system_service.haptics

actual class HapticFeedback {
    actual fun tick() {}

    actual fun click() {}

    actual fun doubleClick() {}

    actual fun heavyClick() {}

    actual fun customDuration(durationMillis: Long) {}
}