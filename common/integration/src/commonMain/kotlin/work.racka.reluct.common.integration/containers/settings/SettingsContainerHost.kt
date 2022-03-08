package work.racka.reluct.common.integration.containers.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.states.settings.SettingsSideEffect
import work.racka.reluct.common.model.states.settings.SettingsState

interface SettingsContainerHost {

    val uiState: StateFlow<SettingsState.State>

    val sideEffect: Flow<SettingsSideEffect>

    fun saveThemeSettings(themeValue: Int)
}