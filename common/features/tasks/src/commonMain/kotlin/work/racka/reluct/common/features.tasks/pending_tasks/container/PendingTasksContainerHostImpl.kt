package work.racka.reluct.common.features.tasks.pending_tasks.container

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class PendingTasksContainerHostImpl(
    private val pendingTasks: PendingTasks,
    private val backgroundDispatcher: CoroutineDispatcher,
    scope: CoroutineScope
) : PendingTasksContainerHost, ContainerHost<TasksState, TasksSideEffect> {

    override val container: Container<TasksState, TasksSideEffect> =
        scope.container(TasksState.Loading) {
            getCompletedTasks()
        }

    override val uiState: StateFlow<TasksState>
        get() = container.stateFlow

    override val sideEffect: Flow<TasksSideEffect>
        get() = container.sideEffectFlow

    private fun getCompletedTasks() = intent {
        val tasks = withContext(backgroundDispatcher) {
            pendingTasks.getTasks()
        }
        tasks.collectLatest { taskList ->
            val overdueList = taskList.filter { it.overdue }
            val grouped = taskList
                .filterNot { it.overdue }
                .groupBy { it.dueDate }
            reduce {
                TasksState.PendingTasks(
                    tasks = grouped,
                    overdueTasks = overdueList
                )
            }
        }
    }

    override fun toggleDone(taskId: Long, isDone: Boolean) = intent {
        withContext(backgroundDispatcher) {
            pendingTasks.toggleTaskDone(taskId, isDone)
        }
        postSideEffect(TasksSideEffect.TaskDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: Long) = intent {
        postSideEffect(TasksSideEffect.Navigation.NavigateToTaskDetails(taskId))
    }
}