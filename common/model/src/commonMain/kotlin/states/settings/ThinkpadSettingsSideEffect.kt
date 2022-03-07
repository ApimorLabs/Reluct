package states.settings

sealed class ThinkpadSettingsSideEffect {
    data class ApplyThemeOption(val themeValue: Int) : ThinkpadSettingsSideEffect()
    data class ApplySortOption(val sortValue: Int) : ThinkpadSettingsSideEffect()
}
