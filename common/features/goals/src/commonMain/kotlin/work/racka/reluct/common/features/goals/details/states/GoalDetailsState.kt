package work.racka.reluct.common.features.goals.details.states

import work.racka.reluct.common.model.domain.goals.Goal

sealed class GoalDetailsState {
    data class Data(val goal: Goal) : GoalDetailsState()
    object NotFound : GoalDetailsState()
    object Loading : GoalDetailsState()
}
