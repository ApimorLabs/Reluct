package work.racka.reluct.android.compose.components.images

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.FileResource

@Composable
expect fun LottieAnimationWithDescription(
    lottieResource: FileResource,
    description: String?,
    modifier: Modifier = Modifier,
    imageSize: Dp = 148.dp,
    iterations: Int = 1,
    descriptionTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
)
