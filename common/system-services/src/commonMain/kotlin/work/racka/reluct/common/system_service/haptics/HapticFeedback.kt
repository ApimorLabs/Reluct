package work.racka.reluct.common.system_service.haptics

expect class HapticFeedback {
    fun tick()
    fun click()
    fun doubleClick()
    fun customDuration()
}