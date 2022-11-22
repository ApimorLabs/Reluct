package work.racka.reluct.compose.common.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.FileResource

/**
 * Can not provide default values for [descriptionTextStyle] from MaterialTheme
 * Because of: https://github.com/JetBrains/compose-jb/issues/1407
 */
@Composable
expect fun LottieAnimationWithDescription(
    lottieResource: FileResource,
    description: String?,
    descriptionTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    imageSize: Dp = 148.dp,
    iterations: Int = 1,
)
