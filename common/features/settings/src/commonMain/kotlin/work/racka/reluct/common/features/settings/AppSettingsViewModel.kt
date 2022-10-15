package work.racka.reluct.common.features.settings

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.features.screen_time.services.ScreenTimeServices
import work.racka.reluct.common.features.settings.states.LimitSettings
import work.racka.reluct.common.features.settings.states.SettingsEvents
import work.racka.reluct.common.features.settings.states.SettingsState
import work.racka.reluct.common.settings.MultiplatformSettings

class AppSettingsViewModel(
    private val settings: MultiplatformSettings,
    private val screenTimeServices: ScreenTimeServices
) : CommonViewModel() {

    private val limitSettings = combine(
        settings.doNoDisturb,
        settings.focusMode,
        settings.appBlockingEnabled
    ) { dnd, focusMode, appBlocking ->
        LimitSettings(
            dndOn = dnd,
            focusModeOn = focusMode,
            appBlockingEnabled = appBlocking
        )
    }

    val uiState: StateFlow<SettingsState> = combine(
        settings.theme, limitSettings
    ) { themeSelected, limitSettings ->
        SettingsState(
            themeValue = themeSelected,
            limitSettings = limitSettings
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val _events = Channel<SettingsEvents>()
    val events: Flow<SettingsEvents> = _events.receiveAsFlow()

    fun saveThemeSettings(themeValue: Int) {
        val saved = settings.saveThemeSettings(themeValue)
        if (saved) {
            _events.trySend(SettingsEvents.ThemeChanged(themeValue))
        }
    }

    fun toggleDnd(value: Boolean) {
        vmScope.launch {
            settings.saveDoNotDisturb(value)
            _events.send(SettingsEvents.DndChanged(value))
        }
    }

    fun toggleFocusMode(value: Boolean) {
        vmScope.launch {
            settings.saveFocusMode(value)
            _events.send(SettingsEvents.FocusModeChanged(value))
        }
    }

    fun toggleAppBlocking(value: Boolean) {
        vmScope.launch {
            if (settings.saveAppBlocking(value)) {
                _events.send(SettingsEvents.AppBlockingChanged(value))
                if (value) screenTimeServices.startLimitsService()
                else screenTimeServices.stopLimitsService()
            }
        }
    }
}