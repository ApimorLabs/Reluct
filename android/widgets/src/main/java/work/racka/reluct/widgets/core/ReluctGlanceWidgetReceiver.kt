package work.racka.reluct.widgets.core

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import org.koin.core.component.KoinComponent

abstract class ReluctGlanceWidgetReceiver<T : ReluctGlanceWidget<*>> :
    GlanceAppWidgetReceiver(),
    KoinComponent {

    override val glanceAppWidget: GlanceAppWidget
        get() = createWidget().apply {
            initiateLoad()
        }

    abstract fun createWidget(): T
}
