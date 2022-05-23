package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

data class SearchTasksState(
    val searchData: SearchData = SearchData.Loading(),
    val searchQuery: String = "",
    val shouldUpdateData: Boolean = true,
)

sealed class SearchData(
    val tasksData: List<Task>,
) {
    data class Data(
        private val tasks: List<Task>,
    ) : SearchData(tasks)

    class Loading(
        tasks: List<Task> = emptyList(),
    ) : SearchData(tasks)

    object Empty : SearchData(tasksData = emptyList())
}
