package work.racka.reluct.common.system_service.haptics

internal class DesktopHapticFeedback : HapticFeedback {
    override fun tick() {}

    override fun click() {}

    override fun doubleClick() {}

    override fun heavyClick() {}

    override fun cascadeFall() {}

    override fun spinAndFall() {}

    override fun customDuration(durationMillis: Long) {}
}