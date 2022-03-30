package work.racka.reluct.common.integration.containers.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import work.racka.reluct.common.model.states.settings.SettingsSideEffect
import work.racka.reluct.common.model.states.settings.SettingsState
import work.racka.reluct.common.settings.repository.MultiplatformSettings

internal class SettingsContainerHostImpl(
    private val settings: MultiplatformSettings,
    scope: CoroutineScope
) : SettingsContainerHost, ContainerHost<SettingsState.State, SettingsSideEffect> {

    override val container: Container<SettingsState.State, SettingsSideEffect> =
        scope.container(SettingsState.DefaultState) {
            readSettings()
        }

    override val uiState: StateFlow<SettingsState.State>
        get() = container.stateFlow

    override val sideEffect: Flow<SettingsSideEffect>
        get() = container.sideEffectFlow

    override fun saveThemeSettings(themeValue: Int) = intent {
        settings.saveThemeSettings(themeValue)
        postSideEffect(SettingsSideEffect.ApplyThemeOption(themeValue))
    }

    // Read Settings Values
    private fun readSettings() {
        readThemeSettings()
    }

    private fun readThemeSettings() = intent {
        settings.theme.collectLatest { themeValue ->
            reduce { state.copy(themeValue = themeValue) }
        }
    }
}