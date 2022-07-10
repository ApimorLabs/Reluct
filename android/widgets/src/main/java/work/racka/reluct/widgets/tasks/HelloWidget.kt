package work.racka.reluct.widgets.tasks

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.text.Text

class HelloWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    @Composable
    override fun Content() {
        println("Hello Widget content called")
        Text(text = "Hello Widget")
    }
}