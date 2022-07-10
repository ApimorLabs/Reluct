package work.racka.reluct.widgets.tasks

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class PendingTasksWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = PendingTasksWidget().apply {
            initiateLoad()
        }
}