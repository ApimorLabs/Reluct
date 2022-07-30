package work.racka.reluct.android.compose.components.cards.headers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.theme.Dimens

@Composable
fun ListGroupHeadingHeader(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RectangleShape,
    tonalElevation: Dp = 0.dp
) {
    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = modifier
                    .padding(bottom = Dimens.ExtraSmallPadding.size)
                    .padding(horizontal = Dimens.SmallPadding.size),
                text = text,
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = contentColor
            )
        }
    }
}