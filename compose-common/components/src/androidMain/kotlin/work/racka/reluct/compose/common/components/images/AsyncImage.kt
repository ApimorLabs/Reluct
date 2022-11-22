package work.racka.reluct.compose.common.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import work.racka.reluct.common.model.domain.core.Icon

@Composable
actual fun rememberAsyncImage(
    url: String,
    onStartLoading: () -> Unit,
    onFinishLoading: (isSuccess: Boolean) -> Unit
): Painter = rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .crossfade(true)
        .listener(
            onStart = { onStartLoading() },
            onSuccess = { _, _ -> onFinishLoading(true) },
            onError = { _, _ -> onFinishLoading(false) },
            onCancel = { onFinishLoading(false) }
        )
        .build()
)

@Composable
actual fun painterResource(mppIcon: Icon): Painter = rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .data(mppIcon.icon).build()
)
