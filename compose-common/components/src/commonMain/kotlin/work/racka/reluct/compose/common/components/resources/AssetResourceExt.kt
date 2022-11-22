package work.racka.reluct.compose.common.components.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.AssetResource

@Composable
expect fun painterResource(resource: AssetResource): Painter
