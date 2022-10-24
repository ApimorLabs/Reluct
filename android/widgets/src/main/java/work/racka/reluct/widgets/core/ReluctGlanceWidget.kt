package work.racka.reluct.widgets.core

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.glance.GlanceId
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class ReluctGlanceWidget<T>(initialData: T? = null) :
    GlanceAppWidget(), KoinComponent {
    private val context: Context by inject()

    private var glanceId by mutableStateOf<GlanceId?>(null)
    private var size by mutableStateOf<DpSize?>(null)
    private var data by mutableStateOf<T?>(initialData)

    private val coroutineScope = MainScope()
    private var loadJob: Job? = null

    abstract suspend fun loadData(): T

    fun initiateLoad() {
        loadJob?.cancel()
        loadJob = coroutineScope.launch {
            data = loadData()

            println("Glance ID is: $glanceId")
            val currentGlanceId = snapshotFlow { glanceId }.filterNotNull().firstOrNull()
            currentGlanceId?.run {
                println("Updating Widget of ID: $this")
                update(context, this)
            }
        }
    }

    @Composable
    override fun Content() {
        glanceId = LocalGlanceId.current
        size = LocalSize.current

        Content(data)
    }

    @Composable
    abstract fun Content(data: T?)
}