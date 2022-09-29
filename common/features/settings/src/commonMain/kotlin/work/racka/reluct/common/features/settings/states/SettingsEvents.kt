package work.racka.reluct.common.features.settings.states

sealed class SettingsEvents {
    class ThemeChanged(val themeValue: Int) : SettingsEvents()
    class FocusModeChanged(val isEnabled: Boolean) : SettingsEvents()
    class DndChanged(val isEnabled: Boolean) : SettingsEvents()
}