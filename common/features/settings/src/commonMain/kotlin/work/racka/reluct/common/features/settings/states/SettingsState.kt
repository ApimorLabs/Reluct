package work.racka.reluct.common.features.settings.states

import work.racka.reluct.common.settings.Keys

data class SettingsState(
    val themeValue: Int = Keys.Defaults.THEME,
    val limitSettings: LimitSettings = LimitSettings()
)

data class LimitSettings(
    val dndOn: Boolean = false,
    val focusModeOn: Boolean = false
)
