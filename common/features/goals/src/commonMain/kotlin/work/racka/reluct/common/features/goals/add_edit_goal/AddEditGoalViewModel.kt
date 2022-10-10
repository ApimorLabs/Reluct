package work.racka.reluct.common.features.goals.add_edit_goal

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.goals.GetGoals
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.features.goals.active.states.DefaultGoals
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.add_edit_goal.states.AddEditGoalState
import work.racka.reluct.common.model.domain.goals.Goal

/**
 * [goalId] is passed when you want to edit a saved Goal and will be null for new goals
 * [defaultGoalIndex] is provided to get a predefined Goal from the list shown on the UI
 */
class AddEditGoalViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals,
    private val goalId: String?,
    private val defaultGoalIndex: Int?
) : CommonViewModel() {

    private val state: MutableStateFlow<AddEditGoalState> =
        MutableStateFlow(AddEditGoalState.Loading)
    val uiState: StateFlow<AddEditGoalState> = state.asStateFlow()

    private val eventsChannel = Channel<GoalsEvents>(Channel.UNLIMITED)
    val events: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    init {
        getGoal(goalId)
    }

    fun saveCurrentGoal() {
        vmScope.launch {
            val goalState = state.value
            if (goalState is AddEditGoalState.Data) {
                modifyGoals.saveGoal(goalState.goal)
                val result =
                    eventsChannel.trySend(GoalsEvents.GoalSavedMessage(goalState.goal.name))
                result.onSuccess {
                    /**
                     * Go back after saving if you are just editing a Goal and the [goalId]
                     * is not null else just show the GoalSaved State for adding more tasks
                     */
                    goalId?.run { eventsChannel.send(GoalsEvents.Exit) }
                        ?: state.update { AddEditGoalState.Saved }
                }
            }
        }
    }

    fun newGoal() {
        state.update { AddEditGoalState.Data(isEdit = false, goal = DefaultGoals.emptyGoal()) }
    }

    fun updateCurrentGoal(goal: Goal) {
        state.update { AddEditGoalState.Data(isEdit = goalId != null, goal = goal) }
    }

    private fun getGoal(id: String?) {
        vmScope.launch {
            if (defaultGoalIndex != null) getPredefinedGoal(defaultGoalIndex)
            else
                when (id) {
                    null -> state.update {
                        AddEditGoalState.Data(
                            isEdit = false, goal = DefaultGoals.emptyGoal()
                        )
                    }
                    else -> {
                        when (val goal = getGoals.getGoal(id).firstOrNull()) {
                            null -> state.update { AddEditGoalState.NotFound }
                            else -> state.update { AddEditGoalState.Data(goal, isEdit = true) }
                        }
                    }
                }
        }
    }

    private fun getPredefinedGoal(index: Int) {
        val goal = DefaultGoals.predefined().getOrNull(index)
        state.update {
            AddEditGoalState.Data(
                isEdit = false,
                goal = goal ?: DefaultGoals.emptyGoal()
            )
        }
    }
}