package work.racka.reluct.common.system_service.haptics

interface HapticFeedback {
    fun tick()
    fun click()
    fun doubleClick()
    fun heavyClick()
    fun cascadeFall()
    fun spinAndFall()
    fun customDuration(durationMillis: Long)
}