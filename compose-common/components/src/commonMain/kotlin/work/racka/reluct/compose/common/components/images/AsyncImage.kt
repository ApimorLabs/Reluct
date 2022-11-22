package work.racka.reluct.compose.common.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import work.racka.reluct.common.model.domain.core.Icon

@Composable
expect fun rememberAsyncImage(
    url: String,
    onStartLoading: () -> Unit = {},
    onFinishLoading: (isSuccess: Boolean) -> Unit = {}
): Painter

@Composable
expect fun painterResource(mppIcon: Icon): Painter
