package work.racka.reluct.common.features.goals.active

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.goals.GetGoals
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.active.states.GoalsListState
import work.racka.reluct.common.model.domain.goals.Goal

class ActiveGoalsViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals
) : CommonViewModel() {

    private val goalUiState: MutableStateFlow<GoalsListState> =
        MutableStateFlow(GoalsListState.Loading())
    val uiState: StateFlow<GoalsListState> = goalUiState.asStateFlow()

    private val eventsChannel = Channel<GoalsEvents>(capacity = Channel.UNLIMITED)
    val event: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private var activeGoalsJob: Job? = null

    init {
        getActiveGoals(limitFactor)
    }

    private fun getActiveGoals(limitFactor: Long) {
        activeGoalsJob?.cancel()
        activeGoalsJob = vmScope.launch {
            getGoals.getActiveGoals(factor = limitFactor).collectLatest { goals ->
                goalUiState.update {
                    newDataPresent = it.goals != goals
                    GoalsListState.Data(goalsData = goals, newDataPresent = newDataPresent)
                }
            }
        }
    }

    fun fetchMoreData() {
        if (newDataPresent && goalUiState.value !is GoalsListState.Loading) {
            limitFactor++
            activeGoalsJob?.cancel()
            goalUiState.update { GoalsListState.Loading(it.goals, newDataPresent) }
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