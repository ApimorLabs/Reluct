package work.racka.reluct.common.features.tasks.task_details.container

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
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

class TaskDetailsContainerHostImpl(
    private val taskDetails: TaskDetailsRepository,
    scope: CoroutineScope
) : TaskDetailsContainerHost, ContainerHost<TasksState, TasksSideEffect> {

    override val container: Container<TasksState, TasksSideEffect> =
        scope.container(TasksState.Loading)

    override val uiState: StateFlow<TasksState>
        get() = container.stateFlow

    override val sideEffect: Flow<TasksSideEffect>
        get() = container.sideEffectFlow

    override fun getTask(taskId: Long) = intent {
        taskDetails.getTask(taskId).collectLatest { task ->
            when (task) {
                null -> {
                    reduce { TasksState.EmptyTaskDetails }
                    postSideEffect(
                        TasksSideEffect.DisplayErrorMsg(
                            Constants.TASK_NOT_FOUND
                        )
                    )
                }
                else -> {
                    reduce { TasksState.TaskDetails(task) }
                }
            }
        }
    }

    override fun toggleDone(taskId: Long, isDone: Boolean) = intent {
        taskDetails.toggleTask(taskId, isDone)
        postSideEffect(TasksSideEffect.TaskDone(isDone))
    }

    override fun editTask(taskId: Long) = intent {
        postSideEffect(TasksSideEffect.Navigation.NavigateToEdit(taskId))
    }

    override fun deleteTask(taskId: Long) = intent {
        taskDetails.deleteTask(taskId)
        postSideEffect(
            TasksSideEffect.ShowSnackbar(Constants.DELETED_SUCCESSFULLY)
        )
    }

    override fun goBack() = intent {
        postSideEffect(TasksSideEffect.Navigation.GoBack)
    }
}