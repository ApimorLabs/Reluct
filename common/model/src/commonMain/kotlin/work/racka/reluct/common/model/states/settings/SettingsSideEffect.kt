package work.racka.reluct.common.model.states.settings

sealed class SettingsSideEffect {
    data class ApplyThemeOption(val themeValue: Int) : SettingsSideEffect()
}
