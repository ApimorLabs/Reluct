package work.racka.reluct.common.features.screen_time.limits

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.limits.ManageDistractingApps
import work.racka.reluct.common.data.usecases.limits.ManageFocusMode
import work.racka.reluct.common.data.usecases.limits.ManagePausedApps
import work.racka.reluct.common.features.screen_time.limits.states.*
import work.racka.reluct.common.features.screen_time.util.Constants

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

    private var focusModeJob: Job? = null
    private var distractingAppsJob: Job? = null
    private var pausedAppsJob: Job? = null

    init {
        initialize()
    }

    private fun initialize() {
        getFocusMode()
        getPausedApps()
    }

    fun getDistractingApps() {
        distractingAppsJob?.cancel()
        distractingAppsState.update { DistractingAppsState.Loading() }
        distractingAppsJob = vmScope.launch {
            manageDistractingApps().collectLatest { apps ->
                // First is distracting apps, Second is Non Distracting apps
                distractingAppsState.update {
                    DistractingAppsState.Data(
                        distractingApps = apps.first,
                        otherApps = apps.second
                    )
                }
            }
        }
    }

    fun toggleFocusMode(value: Boolean) {
        vmScope.launch {
            manageFocusMode.toggleFocusMode(value)
            if (value) {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        true,
                        Constants.FOCUS_MODE_ON
                    )
                )
            } else {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        false,
                        Constants.FOCUS_MODE_OFF
                    )
                )
            }
        }
    }

    fun toggleDnd(value: Boolean) {
        vmScope.launch {
            manageFocusMode.toggleDoNoDisturb(value)
            if (value) {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        true,
                        Constants.DND_ON
                    )
                )
            } else {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        false,
                        Constants.DND_OFF
                    )
                )
            }
        }
    }

    fun pauseApp(packageName: String) {
        vmScope.launch {
            managePausedApps.pauseApp(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    true,
                    Constants.MARK_PAUSED
                )
            )
        }
    }

    fun unPauseApp(packageName: String) {
        vmScope.launch {
            managePausedApps.unPauseApp(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    false,
                    Constants.UN_MARK_PAUSED
                )
            )
        }
    }

    fun markAsDistracting(packageName: String) {
        vmScope.launch {
            manageDistractingApps.markAsDistracting(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    true,
                    Constants.MARK_DISTRACTING
                )
            )
        }
    }

    fun markAsNonDistracting(packageName: String) {
        vmScope.launch {
            manageDistractingApps.markAsNotDistracting(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    false,
                    Constants.UN_MARK_DISTRACTING
                )
            )
        }
    }

    private fun getFocusMode() {
        focusModeJob?.cancel()
        focusModeJob = vmScope.launch {
            val focus = combine(
                manageFocusMode.isFocusModeOn,
                manageFocusMode.isDoNotDisturbOn
            ) { focusMode, dnd ->
                FocusModeState(
                    focusModeOn = focusMode,
                    doNotDisturbOn = dnd
                )
            }
            focus.collect { state ->
                focusModeState.update { state }
            }
        }
    }

    private fun getPausedApps() {
        pausedAppsJob?.cancel()
        pausedAppsState.update { PausedAppsState.Loading() }
        pausedAppsJob = vmScope.launch {
            managePausedApps().collectLatest { apps ->
                // First is paused apps, Second is un paused apps
                pausedAppsState.update {
                    PausedAppsState.Data(
                        pausedApps = apps.first,
                        unPausedApps = apps.second
                    )
                }
            }
        }
    }

    private fun cancelAllJobs() {
        focusModeJob?.cancel()
        pausedAppsJob?.cancel()
        distractingAppsJob?.cancel()
    }

    override fun destroy() {
        cancelAllJobs()
        super.destroy()
    }
}