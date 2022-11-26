package work.racka.reluct.ui.navigationComponents.graphs.tasks

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.TasksConfig
import work.racka.reluct.common.core.navigation.destination.graphs.TasksExtraConfig
import work.racka.reluct.ui.screens.ComposeRenderer
import work.racka.reluct.ui.screens.tasks.addEdit.AddEditTaskComponent
import work.racka.reluct.ui.screens.tasks.completed.CompletedTasksComponent
import work.racka.reluct.ui.screens.tasks.details.TaskDetailsComponent
import work.racka.reluct.ui.screens.tasks.pending.PendingTasksComponent
import work.racka.reluct.ui.screens.tasks.search.SearchTasksComponent
import work.racka.reluct.ui.screens.tasks.statistics.TasksStatisticsComponent

interface TasksComponent : ComposeRenderer {

    val mainChildStack: Value<ChildStack<TasksConfig, MainChild>>
    val itemsChildStack: Value<ChildStack<TasksExtraConfig, ItemsChild>>

    sealed class MainChild {
        data class Completed(val component: CompletedTasksComponent) : MainChild()
        data class Pending(val component: PendingTasksComponent) : MainChild()
        data class Search(val component: SearchTasksComponent) : MainChild()
        class Statistics(val component: TasksStatisticsComponent) : MainChild()
    }

    sealed class ItemsChild {
        data class AddEdit(val component: AddEditTaskComponent) : ItemsChild()
        data class Details(val component: TaskDetailsComponent) : ItemsChild()
        object None : ItemsChild()
    }
}
