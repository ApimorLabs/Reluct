package work.racka.reluct.common.features.goals.active.states

data class ActiveGoalsState(
    val isSyncing: Boolean = false,
    val goalsListState: GoalsListState = GoalsListState.Loading()
)
