package work.racka.reluct.compose.common.components.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter
import dev.icerock.moko.resources.AssetResource

fun AssetResource.toAndroidAssetUri() = "file:///android_asset/$originalPath"

@Composable
actual fun painterResource(resource: AssetResource): Painter =
    rememberAsyncImagePainter(model = resource.toAndroidAssetUri())
