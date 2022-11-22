package work.racka.reluct.compose.common.components.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.icerock.moko.resources.FileResource
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.theme.Dimens

@Composable
actual fun LottieAnimationWithDescription(
    lottieResource: FileResource,
    description: String?,
    descriptionTextStyle: TextStyle,
    modifier: Modifier,
    imageSize: Dp,
    iterations: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
    ) {
        Image(
            modifier = Modifier.size(imageSize),
            painter = painterResource(SharedRes.assets.permissions_unlock),
            contentDescription = description
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
