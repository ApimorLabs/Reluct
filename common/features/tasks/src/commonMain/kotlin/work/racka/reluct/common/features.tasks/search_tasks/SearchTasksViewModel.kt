package work.racka.reluct.common.features.tasks.search_tasks

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.SearchData
import work.racka.reluct.common.model.states.tasks.SearchTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

class SearchTasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase
) : CommonViewModel() {

    private val searchData: MutableStateFlow<SearchData> = MutableStateFlow(SearchData.Loading())
    private val searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    private val shouldUpdateData: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val uiState: StateFlow<SearchTasksState> = combine(
        searchData, searchQuery, shouldUpdateData
    ) { searchData, searchQuery, shouldUpdateData ->
        SearchTasksState(
            searchData = searchData,
            searchQuery = searchQuery,
            shouldUpdateData = shouldUpdateData
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchTasksState()
    )

    private val _events: Channel<TasksEvents> = Channel()
    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L

    private lateinit var searchTasksJob: Job

    init {
        getTasks()
    }

    private fun getTasks() {
        searchTasksJob = vmScope.launch {
            getTasksUseCase.getSearchedTasks(searchQuery.value, limitFactor)
                .collectLatest { tasks ->
                    if (tasks.isEmpty() || searchQuery.value.isBlank()) {
                        searchData.update { SearchData.Empty }
                    } else {
                        searchData.update {
                            shouldUpdateData.value = it.tasksData != tasks
                            SearchData.Data(tasks)
                        }
                    }
                }
        }
    }

    fun search(query: String) {
        searchTasksJob.cancel() // Cancel the current collection job
        searchData.update { SearchData.Loading(tasks = it.tasksData) }
        searchQuery.update { query }
        getTasks()
    }

    fun fetchMoreData() {
        if (shouldUpdateData.value) {
            limitFactor++
            searchTasksJob.cancel() // Cancel the current collection job
            searchData.update { SearchData.Loading(tasks = it.tasksData) }
            getTasks()
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone, task.title))
    }

    fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }

    fun goBack() {
        _events.trySend(TasksEvents.Navigation.GoBack)
    }
}