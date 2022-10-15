package work.racka.reluct.common.features.goals.details.states

import work.racka.reluct.common.model.domain.goals.Goal

sealed class GoalDetailsState(val isSyncing: Boolean) {
    data class Data(
        val goal: Goal,
        private val syncingData: Boolean
    ) : GoalDetailsState(syncingData)

    object NotFound : GoalDetailsState(false)
    object Loading : GoalDetailsState(false)
}
