package work.racka.reluct.widgets.tasks.actions

import android.content.Context
import android.os.Parcelable
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.widgets.tasks.PendingTasksWidget

internal object PendingTasksWidgetParamKeys {
    val TASK_DONE_KEY = ActionParameters.Key<Boolean>("TASK_DONE_KEY")
    val TASK_KEY = ActionParameters.Key<WidgetTaskParcel>("TASK_KEY")
}

internal class ToggleTaskDoneAction : ActionCallback, KoinComponent {
    private val modifyTasks: ModifyTaskUseCase by inject()

    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val task = parameters[PendingTasksWidgetParamKeys.TASK_KEY]?.asTask()
        task?.run {
            modifyTasks.toggleTaskDone(this, !this.done)
        }

        // Update the widget
        PendingTasksWidget().apply {
            initiateLoad()
            update(context, glanceId)
        }
    }
}

class OpenTaskDetailsAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {

    }
}

@Parcelize
internal data class WidgetTaskParcel(
    val id: String,
    val title: String,
    val description: String,
    val done: Boolean,
    val overdue: Boolean,
    val dueDate: String,
    val dueTime: String,
    val timeLeftLabel: String,
    val reminder: String,
    val completedDateAndTime: String
) : Parcelable

internal fun WidgetTaskParcel.asTask() = Task(
    id = id,
    title = title,
    description = description,
    done = done,
    overdue = overdue,
    dueDate = dueDate,
    dueTime = dueTime,
    timeLeftLabel = timeLeftLabel,
    reminder = reminder,
    completedDateAndTime = completedDateAndTime
)

internal fun Task.asWidgetTaskParcel() = WidgetTaskParcel(
    id = id,
    title = title,
    description = description,
    done = done,
    overdue = overdue,
    dueDate = dueDate,
    dueTime = dueTime,
    timeLeftLabel = timeLeftLabel,
    reminder = reminder,
    completedDateAndTime = completedDateAndTime
)