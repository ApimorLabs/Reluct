package work.racka.reluct.common.model.states.settings

sealed class SettingsEvents {
    class ThemeChanged(val themeValue: Int) : SettingsEvents()
}
