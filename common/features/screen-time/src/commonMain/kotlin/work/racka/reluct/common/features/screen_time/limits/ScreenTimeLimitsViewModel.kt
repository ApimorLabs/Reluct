package work.racka.reluct.common.features.screen_time.limits

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.limits.ManageDistractingApps
import work.racka.reluct.common.data.usecases.limits.ManageFocusMode
import work.racka.reluct.common.data.usecases.limits.ManagePausedApps
import work.racka.reluct.common.features.screen_time.limits.states.*

class ScreenTimeLimitsViewModel(
    private val manageFocusMode: ManageFocusMode,
    private val managePausedApps: ManagePausedApps,
    private val manageDistractingApps: ManageDistractingApps
) : CommonViewModel() {

    private val focusModeState: MutableStateFlow<FocusModeState> =
        MutableStateFlow(FocusModeState())
    private val pausedAppsState: MutableStateFlow<PausedAppsState> =
        MutableStateFlow(PausedAppsState.Nothing)
    private val distractingAppsState: MutableStateFlow<DistractingAppsState> =
        MutableStateFlow(DistractingAppsState.Nothing)

    val uiState: StateFlow<ScreenTimeLimitState> = combine(
        focusModeState, pausedAppsState, distractingAppsState
    ) { focusModeState, pausedAppsState, distractingAppsState ->
        ScreenTimeLimitState(
            focusModeState = focusModeState,
            pausedAppsState = pausedAppsState,
            distractingAppsState = distractingAppsState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScreenTimeLimitState()
    )

    private val _events = Channel<ScreenTimeLimitsEvents>(Channel.UNLIMITED)
    val events: Flow<ScreenTimeLimitsEvents> = _events.receiveAsFlow()
}