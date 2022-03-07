package states.settings

sealed class ThinkpadSettingsState {
    data class State(
        val themeValue: Int = -1,
        val sortValue: Int = 0
    ) : ThinkpadSettingsState()

    companion object {
        val DefaultState = State()
    }
}
