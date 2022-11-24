package work.racka.reluct.ui.navigationComponents.graphs.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.TasksConfig
import work.racka.reluct.ui.screens.tasks.completed.CompletedTasksComponent
import work.racka.reluct.ui.screens.tasks.pending.PendingTasksComponent
import work.racka.reluct.ui.screens.tasks.search.SearchTasksComponent
import work.racka.reluct.ui.screens.tasks.statistics.TasksStatisticsComponent

class MainTasksComponentRouter(
    componentContext: ComponentContext,
    initialStack: List<TasksConfig>,
    private val onAddTask: () -> Unit,
    private val onShowDetails: (taskId: String) -> Unit,
    private val onExit: () -> Unit
) {

    private val navigation = StackNavigation<TasksConfig>()

    val stack: Value<ChildStack<TasksConfig, TasksComponent.MainChild>> = componentContext
        .childStack(
            source = navigation,
            initialStack = { initialStack.ifEmpty { listOf(TasksConfig.Pending) } },
            childFactory = ::createChild,
            key = "TasksMainChild"
        )

    private fun createChild(config: TasksConfig, context: ComponentContext) = when (config) {
        is TasksConfig.Pending -> TasksComponent.MainChild.Pending(createPending(context))
        is TasksConfig.Completed -> TasksComponent.MainChild.Completed(createCompleted(context))
        is TasksConfig.Search -> TasksComponent.MainChild.Search(createSearch(context))
        is TasksConfig.Statistics -> TasksComponent.MainChild.Statistics(createStats(context))
    }

    private fun createPending(context: ComponentContext) = PendingTasksComponent(
        componentContext = context,
        onShowDetails = onShowDetails,
        onAddTask = onAddTask,
        onExit = onExit,
    )

    private fun createCompleted(context: ComponentContext) = CompletedTasksComponent(
        componentContext = context,
        onShowDetails = onShowDetails,
        onAddTask = onAddTask,
        onExit = ::pop,
    )

    private fun createSearch(context: ComponentContext) = SearchTasksComponent(
        componentContext = context,
        onShowDetails = onShowDetails,
        onExit = ::pop,
    )

    private fun createStats(context: ComponentContext) = TasksStatisticsComponent(
        componentContext = context,
        onShowDetails = onShowDetails,
        onExit = ::pop,
    )

    private fun pop() = navigation.pop { isSuccess -> if (!isSuccess) onExit() }

    fun openPendingTasks() = navigation.bringToFront(TasksConfig.Pending)
    fun openCompletedTasks() = navigation.bringToFront(TasksConfig.Completed)
    fun openSearchTasks() = navigation.bringToFront(TasksConfig.Search)
    fun openTasksStatistics() = navigation.bringToFront(TasksConfig.Statistics)
}
