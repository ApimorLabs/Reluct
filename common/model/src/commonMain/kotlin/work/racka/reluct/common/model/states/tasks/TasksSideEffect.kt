package work.racka.reluct.common.model.states.tasks

sealed class TasksSideEffect {
    object Nothing : TasksSideEffect()
    object AddTask : TasksSideEffect()
}
