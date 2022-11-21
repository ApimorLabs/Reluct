package work.racka.reluct.compose.common.components.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.model.domain.core.Icon
import java.io.IOException
import java.net.URL

@Composable
actual fun rememberAsyncImage(
    url: String,
    onStartLoading: () -> Unit,
    onFinishLoading: (isSuccess: Boolean) -> Unit
): Painter {
    val density = LocalDensity.current
    val emptyPainter = rememberVectorPainter(
        defaultWidth = 0.dp,
        defaultHeight = 0.dp,
        autoMirror = false,
        content = { _, _ -> }
    )
    return produceState<Painter>(emptyPainter) {
        value = withContext(Dispatchers.IO) {
            onStartLoading()
            try {
                loadSvgPainterFromUrl(url = url, density = density)
                    .also { onFinishLoading(true) }
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                onFinishLoading(false)
                emptyPainter
            }
        }
    }.value
}

fun loadSvgPainterFromUrl(url: String, density: Density): Painter =
    URL(url).openStream().buffered().use { loadSvgPainter(it, density) }

@Composable
actual fun painterResource(mppIcon: Icon): Painter {
    // TODO: Re implement with new data later
    return rememberVectorPainter(
        defaultWidth = 0.dp,
        defaultHeight = 0.dp,
        autoMirror = false,
        content = { _, _ -> }
    )
}