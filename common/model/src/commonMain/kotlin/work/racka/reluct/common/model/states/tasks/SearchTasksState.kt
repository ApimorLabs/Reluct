package work.racka.reluct.common.model.states.tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.common.model.domain.tasks.Task

data class SearchTasksState(
    val searchData: SearchData = SearchData.Loading(),
    val searchQuery: String = "",
    val shouldUpdateData: Boolean = true,
)

sealed class SearchData(
    val tasksData: ImmutableList<Task>,
) {
    data class Data(
        private val tasks: ImmutableList<Task>,
    ) : SearchData(tasks)

    class Loading(
        tasks: ImmutableList<Task> = persistentListOf(),
    ) : SearchData(tasks)

    object Empty : SearchData(tasksData = persistentListOf())
}
