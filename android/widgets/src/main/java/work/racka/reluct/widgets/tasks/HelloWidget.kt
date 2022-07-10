package work.racka.reluct.widgets.tasks

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.text.Text

class HelloWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        Text(text = "Hello Widget")
    }
}