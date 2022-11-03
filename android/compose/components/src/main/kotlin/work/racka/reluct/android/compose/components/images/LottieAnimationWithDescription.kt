package work.racka.reluct.android.compose.components.images

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import work.racka.reluct.android.compose.theme.Dimens

@Composable
fun LottieAnimationWithDescription(
    @RawRes lottieResId: Int,
    description: String?,
    modifier: Modifier = Modifier,
    imageSize: Dp = 148.dp,
    iterations: Int = 1,
    descriptionTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(lottieResId)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
    ) {
        LottieAnimation(
            modifier = Modifier.size(imageSize),
            composition = composition,
            progress = progress
        )
        description?.let {
            Text(
                text = it,
                style = descriptionTextStyle,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
