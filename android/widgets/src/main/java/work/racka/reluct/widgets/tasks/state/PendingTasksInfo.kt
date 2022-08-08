package work.racka.reluct.widgets.tasks.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import work.racka.reluct.common.model.domain.tasks.Task

@Serializable
internal sealed interface PendingTasksInfo {
    @Serializable
    object Loading : PendingTasksInfo

    @Serializable
    data class Data(
        val pendingTasks: Map<String, List<WidgetTaskParcel>>
    ) : PendingTasksInfo

    @Serializable
    object Nothing : PendingTasksInfo
}

@Serializable
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
    reminderFormatted = reminder,
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
    reminder = reminderFormatted,
    completedDateAndTime = completedDateAndTime
)