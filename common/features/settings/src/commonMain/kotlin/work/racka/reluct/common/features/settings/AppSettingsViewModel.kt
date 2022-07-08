package work.racka.reluct.common.features.settings

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.model.states.settings.SettingsEvents
import work.racka.reluct.common.model.states.settings.SettingsState
import work.racka.reluct.common.settings.MultiplatformSettings

internal class AppSettingsViewModel(
    private val settings: MultiplatformSettings
) : CommonViewModel() {

    private val themeSelected = settings.theme

    private val dnd = MutableStateFlow("TODO")

    val uiState: StateFlow<SettingsState> = combine(
        themeSelected, dnd
    ) { themeSelected, _ ->
        SettingsState(
            themeValue = themeSelected
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val _events = Channel<SettingsEvents>()
    val events: Flow<SettingsEvents>
        get() = _events.receiveAsFlow()

    fun saveThemeSettings(themeValue: Int) {
        val saved = settings.saveThemeSettings(themeValue)
        if (saved) {
            _events.trySend(SettingsEvents.ThemeChanged(themeValue))
        }
    }
}