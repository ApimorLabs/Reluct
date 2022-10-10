package work.racka.reluct.common.features.goals.add_edit_goal.states

import work.racka.reluct.common.model.domain.goals.Goal

sealed class AddEditGoalState {
    data class Data(
        val goal: Goal,
        val isEdit: Boolean
    ) : AddEditGoalState()

    object Saved : AddEditGoalState()

    object Loading : AddEditGoalState()

    object NotFound : AddEditGoalState()
}
