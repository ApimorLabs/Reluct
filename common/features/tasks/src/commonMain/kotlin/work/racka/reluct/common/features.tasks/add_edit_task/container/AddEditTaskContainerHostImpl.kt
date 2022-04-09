package work.racka.reluct.common.features.tasks.add_edit_task.container

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
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepository
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

class AddEditTaskContainerHostImpl(
    private val addEditTask: AddEditTaskRepository,
    private val taskId: Long? = null,
    scope: CoroutineScope
) : AddEditTask, ContainerHost<TasksState, TasksSideEffect> {

    override val container: Container<TasksState, TasksSideEffect> =
        scope.container(TasksState.Loading)

    override val uiState: StateFlow<TasksState>
        get() = container.stateFlow

    override val events: Flow<TasksSideEffect>
        get() = container.sideEffectFlow

    override fun getTask() = intent {
        when (taskId) {
            null -> reduce { TasksState.EmptyAddEditTask }
            else -> addEditTask.getTaskToEdit(taskId).collectLatest { task ->
                when (task) {
                    null -> reduce { TasksState.EmptyAddEditTask }
                    else -> reduce { TasksState.AddEditTask(task) }
                }
            }
        }
    }

    override fun saveTask(task: EditTask) = intent {
        addEditTask.addTask(task)
        postSideEffect(TasksSideEffect.ShowSnackbar(Constants.TASK_SAVED))
    }

    override fun goBack() = intent {
        postSideEffect(TasksSideEffect.Navigation.GoBack)
    }
}