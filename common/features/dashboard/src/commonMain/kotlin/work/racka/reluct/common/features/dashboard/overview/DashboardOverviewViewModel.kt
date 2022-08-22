package work.racka.reluct.common.features.dashboard.overview

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.dashboard.overview.states.DashboardEvents
import work.racka.reluct.common.features.dashboard.overview.states.DashboardState
import work.racka.reluct.common.features.dashboard.overview.states.TodayScreenTimeState
import work.racka.reluct.common.features.dashboard.overview.states.TodayTasksState

class DashboardOverviewViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase
) : CommonViewModel() {

    private val todayScreenTimeState: MutableStateFlow<TodayScreenTimeState> =
        MutableStateFlow(TodayScreenTimeState.Nothing)
    private val todayTasksState: MutableStateFlow<TodayTasksState> =
        MutableStateFlow(TodayTasksState.Nothing)

    val uiState: StateFlow<DashboardState> = combine(
        todayScreenTimeState, todayTasksState
    ) { todayScreenTimeState, todayTasksState ->
        DashboardState(
            todayScreenTimeState = todayScreenTimeState,
            todayTasksState = todayTasksState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardState()
    )

    private val _events = Channel<DashboardEvents>(Channel.UNLIMITED)
    val events: Flow<DashboardEvents> = _events.receiveAsFlow()
}