package work.racka.reluct.common.features.goals.details

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.goals.GetGoals
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.details.states.GoalDetailsState
import work.racka.reluct.common.model.domain.goals.Goal

class GoalDetailsViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals,
    private val goalId: String?
) : CommonViewModel() {

    private val state: MutableStateFlow<GoalDetailsState> =
        MutableStateFlow(GoalDetailsState.Loading)
    val uiState: StateFlow<GoalDetailsState> = state.asStateFlow()

    private val eventsChannel = Channel<GoalsEvents>(Channel.UNLIMITED)
    val events: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    init {
        getGoal()
    }

    fun toggleGoalActiveState(goalId: String, isActive: Boolean) {
        vmScope.launch {
            modifyGoals.toggleGoalActiveState(isActive, goalId)
            eventsChannel.send(GoalsEvents.ChangedGoalState(isActive, ""))
        }
    }

    fun deleteGoal(goal: Goal) {
        vmScope.launch {
            modifyGoals.deleteGoal(goal.id)
            val result = eventsChannel.trySend(GoalsEvents.DeletedGoal(goal.id, goal.name))
            result.onSuccess { eventsChannel.send(GoalsEvents.Exit) }
        }
    }

    private fun getGoal() {
        vmScope.launch {
            when (goalId) {
                null -> state.update { GoalDetailsState.NotFound }
                else -> getGoals.getGoal(goalId).collectLatest { goal ->
                    when (goal) {
                        null -> state.update { GoalDetailsState.NotFound }
                        else -> state.update { GoalDetailsState.Data(goal) }
                    }
                }
            }
        }
    }
}