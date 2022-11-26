package work.racka.reluct.ui.navigationComponents.graphs.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.TasksExtraConfig
import work.racka.reluct.ui.screens.tasks.addEdit.AddEditTaskComponent
import work.racka.reluct.ui.screens.tasks.details.TaskDetailsComponent

class TaskItemsComponentsRouter(
    componentContext: ComponentContext,
    initialStack: List<TasksExtraConfig>
) {

    private val navigation = StackNavigation<TasksExtraConfig>()

    val stack: Value<ChildStack<TasksExtraConfig, TasksComponent.ItemsChild>> = componentContext
        .childStack(
            source = navigation,
            initialStack = { (listOf(TasksExtraConfig.None) + initialStack).distinct() },
            childFactory = ::createChild,
            key = "TasksItemsChild"
        )

    private fun createChild(config: TasksExtraConfig, context: ComponentContext) = when (config) {
        is TasksExtraConfig.None -> TasksComponent.ItemsChild.None
        is TasksExtraConfig.AddEdit -> TasksComponent.ItemsChild.AddEdit(
            createAddEdit(
                context = context,
                taskId = config.taskId
            )
        )
        is TasksExtraConfig.Details -> TasksComponent.ItemsChild.Details(
            createDetails(
                context = context,
                taskId = config.taskId
            )
        )
    }

    private fun createAddEdit(context: ComponentContext, taskId: String?) = AddEditTaskComponent(
        componentContext = context,
        taskId = taskId,
        onClose = navigation::pop
    )

    private fun createDetails(context: ComponentContext, taskId: String?) = TaskDetailsComponent(
        componentContext = context,
        taskId = taskId,
        onEdit = ::editTask,
        onClose = navigation::pop
    )

    fun addTask() = navigation.navigate { stack ->
        stack.dropLastWhile { it is TasksExtraConfig.AddEdit }
            .plus(TasksExtraConfig.AddEdit(taskId = null))
    }

    fun editTask(taskId: String?) = navigation.navigate { stack ->
        stack.dropLastWhile { it is TasksExtraConfig.AddEdit }
            .plus(TasksExtraConfig.AddEdit(taskId = taskId))
    }

    fun openDetails(taskId: String?) = navigation.navigate { stack ->
        stack.dropLastWhile { it is TasksExtraConfig.Details }
            .plus(TasksExtraConfig.Details(taskId = taskId))
    }
}
