package work.racka.reluct.common.features.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.states.settings.SettingsEvents
import work.racka.reluct.common.model.states.settings.SettingsState

interface AppSettings {
    val uiState: StateFlow<SettingsState>
    val events: Flow<SettingsEvents>
    fun saveThemeSettings(themeValue: Int)
}