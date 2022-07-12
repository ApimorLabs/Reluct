package work.racka.reluct.widgets.tasks.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.android.compose.navigation.destinations.tasks.TaskDetailsDestination
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.widgets.core.openDeepLinkPendingIntent
import work.racka.reluct.widgets.tasks.state.WidgetTaskParcel
import work.racka.reluct.widgets.tasks.state.asTask

internal object PendingTasksWidgetParamKeys {
    val TASK_ID_KEY = ActionParameters.Key<String>("TASK_ID_KEY")
    val TASK_KEY = ActionParameters.Key<WidgetTaskParcel>("TASK_KEY")
}

internal class ToggleTaskDoneAction : ActionCallback, KoinComponent {
    private val modifyTasks: ModifyTaskUseCase by inject()

    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val task = parameters[PendingTasksWidgetParamKeys.TASK_KEY]?.asTask()
        task?.let {
            modifyTasks.toggleTaskDone(it, !it.done)
        }

        // Update the widget
        PendingTasksSource.initialize()
    }
}

internal class ReloadTasksAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        PendingTasksSource.initialize()
    }
}

internal class OpenTaskDetailsAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val taskId = parameters[PendingTasksWidgetParamKeys.TASK_ID_KEY]
        val uriString = TaskDetailsDestination.taskDetailsDeepLink(taskId)
        val pendingIntent = openDeepLinkPendingIntent(context, uriString)
        println("Pending Intent is present: $pendingIntent")
        pendingIntent?.send()
    }
}