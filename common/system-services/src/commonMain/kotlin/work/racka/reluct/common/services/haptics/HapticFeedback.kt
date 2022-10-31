package work.racka.reluct.common.services.haptics

interface HapticFeedback {
    fun tick()
    fun click()
    fun doubleClick()
    fun heavyClick()
    fun cascadeFall()
    fun spinAndFall()
    fun customDuration(durationMillis: Long)
}
