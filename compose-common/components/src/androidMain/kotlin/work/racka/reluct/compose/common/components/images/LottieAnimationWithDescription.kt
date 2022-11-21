package work.racka.reluct.android.compose.components.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.icerock.moko.resources.AssetResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.icerock.moko.resources.FileResource
import work.racka.reluct.compose.common.theme.Dimens

@Composable
actual fun LottieAnimationWithDescription(
    lottieResource: FileResource,
    description: String?,
    modifier: Modifier,
    imageSize: Dp,
    iterations: Int,
    descriptionTextStyle: TextStyle
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(lottieResource.rawResId)
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
