package work.racka.reluct.android.compose.components.topBar

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.theme.Dimens

@Composable
fun ReluctPageHeading(
    modifier: Modifier = Modifier,
    title: String,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RectangleShape,
    tonalElevation: Dp = 0.dp,
    extraItems: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = Dimens.SmallPadding.size),
                text = title,
                style = titleTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = contentColor,
                textAlign = TextAlign.Start
            )

            extraItems()
        }
    }
}