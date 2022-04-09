package work.racka.reluct.common.features.tasks.completed_tasks.container

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class CompletedTasksContainerHostImpl(
    private val completedTasks: CompletedTasksRepository,
    scope: CoroutineScope
) : CompletedTasks, ContainerHost<TasksState, TasksSideEffect> {

    override val container: Container<TasksState, TasksSideEffect> =
        scope.container(TasksState.Loading) {
            getCompletedTasks()
        }

    override val uiState: StateFlow<TasksState>
        get() = container.stateFlow

    override val events: Flow<TasksSideEffect>
        get() = container.sideEffectFlow

    private fun getCompletedTasks() = intent {
        completedTasks.getTasks().collectLatest { taskList ->
            val grouped = taskList.groupBy { it.dueDate }
            reduce {
                TasksState.CompletedTasks(
                    tasks = grouped
                )
            }
        }
    }

    override fun toggleDone(taskId: Long, isDone: Boolean) = intent {
        completedTasks.toggleTaskDone(taskId, isDone)
        postSideEffect(
            TasksSideEffect.TaskDone(isDone)
        )
    }

    override fun navigateToTaskDetails(taskId: Long) = intent {
        postSideEffect(TasksSideEffect.Navigation.NavigateToTaskDetails(taskId))
    }
}