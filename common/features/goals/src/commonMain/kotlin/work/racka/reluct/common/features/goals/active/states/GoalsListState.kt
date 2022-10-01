package work.racka.reluct.common.features.goals.active.states

import work.racka.reluct.common.model.domain.goals.Goal

sealed class GoalsListState(
    val goals: List<Goal>,
    val shouldUpdateData: Boolean
) {
    data class Data(
        private val goalsData: List<Goal> = emptyList(),
        private val newDataPresent: Boolean = true
    ) : GoalsListState(goals = goalsData, shouldUpdateData = newDataPresent)

    data class Loading(
        private val goalsData: List<Goal> = emptyList(),
        private val newDataPresent: Boolean = true
    ) : GoalsListState(goals = goalsData, shouldUpdateData = newDataPresent)
}


