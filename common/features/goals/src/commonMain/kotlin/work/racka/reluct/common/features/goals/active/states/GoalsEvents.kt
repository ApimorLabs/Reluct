package work.racka.reluct.common.features.goals.active.states

sealed class GoalsEvents {
    object Nothing : GoalsEvents()
    class ChangedGoalState(val isActive: Boolean, val msg: String) : GoalsEvents()
    class DeletedGoal(val goalId: String, val goalName: String) : GoalsEvents()
}