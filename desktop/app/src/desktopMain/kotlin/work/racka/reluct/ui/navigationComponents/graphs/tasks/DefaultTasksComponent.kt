package work.racka.reluct.ui.navigationComponents.graphs.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.TasksConfig
import work.racka.reluct.common.core.navigation.destination.graphs.TasksExtraConfig

class DefaultTasksComponent(
    componentContext: ComponentContext,
    initialMainStack: () -> List<TasksConfig>,
    initialItemsStack: () -> List<TasksExtraConfig>,
    onExit: () -> Unit
) : TasksComponent, ComponentContext by componentContext {

    private val itemsChildRouter = TaskItemsComponentsRouter(
        componentContext = this,
        initialStack = initialItemsStack()
    )

    private val mainChildRouter = MainTasksComponentRouter(
        componentContext = this,
        initialStack = initialMainStack(),
        onAddTask = itemsChildRouter::addTask,
        onShowDetails = itemsChildRouter::openDetails,
        onExit = onExit
    )

    override val mainChildStack: Value<ChildStack<TasksConfig, TasksComponent.MainChild>> =
        mainChildRouter.stack
    override val itemsChildStack: Value<ChildStack<TasksExtraConfig, TasksComponent.ItemsChild>> =
        itemsChildRouter.stack

    @Composable
    override fun Render(modifier: Modifier) {
        TasksRootChildren(
            mainChildStack = mainChildStack,
            itemsChildStack = itemsChildStack,
            onUpdateConfig = { config ->
                when (config) {
                    is TasksConfig.Pending -> mainChildRouter::openPendingTasks
                    is TasksConfig.Completed -> mainChildRouter::openCompletedTasks
                    is TasksConfig.Search -> mainChildRouter::openSearchTasks
                    is TasksConfig.Statistics -> mainChildRouter::openTasksStatistics
                }
            }
        )
    }
}
