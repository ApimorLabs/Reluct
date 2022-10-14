package work.racka.reluct.common.features.goals.active

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.goals.GetGoals
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.features.goals.active.states.ActiveGoalsState
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.active.states.GoalsListState
import work.racka.reluct.common.model.domain.goals.Goal

class ActiveGoalsViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals
) : CommonViewModel() {

    private val goalsListState: MutableStateFlow<GoalsListState> =
        MutableStateFlow(GoalsListState.Loading())
    private val isSyncingData = MutableStateFlow(false)
    val uiState: StateFlow<ActiveGoalsState> = combine(
        goalsListState,
        isSyncingData
    ) { goalsListState, isSyncingData ->
        ActiveGoalsState(
            isSyncing = isSyncingData,
            goalsListState = goalsListState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ActiveGoalsState()
    )

    private val eventsChannel = Channel<GoalsEvents>(capacity = Channel.UNLIMITED)
    val event: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private var activeGoalsJob: Job? = null
    private var syncDataJob: Job? = null

    init {
        getActiveGoals(limitFactor)
        syncData()
    }

    private fun getActiveGoals(limitFactor: Long) {
        activeGoalsJob?.cancel()
        activeGoalsJob = vmScope.launch {
            getGoals.getActiveGoals(factor = limitFactor).collectLatest { goals ->
                goalsListState.update { state ->
                    newDataPresent = (state.goals.firstOrNull()?.id != goals.firstOrNull()?.id)
                            && (state.goals.lastOrNull()?.id != goals.lastOrNull()?.id)
                    GoalsListState.Data(goalsData = goals, newDataPresent = newDataPresent)
                }
            }
        }
    }

    fun syncData() {
        syncDataJob?.cancel()
        syncDataJob = vmScope.launch {
            isSyncingData.update { true }
            modifyGoals.syncGoals()
            isSyncingData.update { false }
        }
    }

    fun fetchMoreData() {
        if (newDataPresent && goalsListState.value !is GoalsListState.Loading) {
            limitFactor++
            activeGoalsJob?.cancel()
            goalsListState.update { GoalsListState.Loading(it.goals, newDataPresent) }
            getActiveGoals(limitFactor)
        }
    }

    fun toggleGoalActiveState(goal: Goal, isActive: Boolean) {
        vmScope.launch {
            modifyGoals.toggleGoalActiveState(isActive = isActive, id = goal.id)
            eventsChannel.send(GoalsEvents.ChangedGoalState(isActive = isActive, msg = ""))
        }
    }
}