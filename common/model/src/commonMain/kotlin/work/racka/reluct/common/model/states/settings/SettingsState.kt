package work.racka.reluct.common.model.states.settings

sealed class SettingsState {
    data class State(
        val themeValue: Int = -1
    ) : SettingsState()

    companion object {
        val DefaultState = State()
    }
}
