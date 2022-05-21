package work.racka.reluct.common.features.tasks.completed_tasks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.CompletedTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

 interface CompletedTasks {
     val uiState: StateFlow<CompletedTasksState>
     val events: Flow<TasksEvents>
     fun fetchMoreData()
     fun toggleDone(task: Task, isDone: Boolean)
     fun navigateToTaskDetails(taskId: String)
 }