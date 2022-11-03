package work.racka.reluct.common.features.goals.active.states

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.common.model.domain.goals.Goal

sealed class GoalsListState(
    val goals: ImmutableList<Goal>,
    val shouldUpdateData: Boolean
) {
    data class Data(
        private val goalsData: ImmutableList<Goal> = persistentListOf(),
        private val newDataPresent: Boolean = true
    ) : GoalsListState(goals = goalsData, shouldUpdateData = newDataPresent)

    data class Loading(
        private val goalsData: ImmutableList<Goal> = persistentListOf(),
        private val newDataPresent: Boolean = true
    ) : GoalsListState(goals = goalsData, shouldUpdateData = newDataPresent)
}


