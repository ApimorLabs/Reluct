package work.racka.reluct.common.features.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import work.racka.reluct.common.model.states.settings.SettingsEvents
import work.racka.reluct.common.model.states.settings.SettingsState
import work.racka.reluct.common.settings.MultiplatformSettings

internal class AppSettingsImpl(
    private val settings: MultiplatformSettings,
    private val scope: CoroutineScope
) : AppSettings {

    private val themeSelected = settings.theme

    private val dnd = MutableStateFlow("TODO")

    override val uiState: StateFlow<SettingsState> = combine(
        themeSelected, dnd
    ) { themeSelected, _ ->
        SettingsState(
            themeValue = themeSelected
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val _events = Channel<SettingsEvents>()
    override val events: Flow<SettingsEvents>
        get() = _events.receiveAsFlow()

    override fun saveThemeSettings(themeValue: Int) {
        val saved = settings.saveThemeSettings(themeValue)
        if (saved) {
            _events.trySend(SettingsEvents.ThemeChanged(themeValue))
        }
    }
}