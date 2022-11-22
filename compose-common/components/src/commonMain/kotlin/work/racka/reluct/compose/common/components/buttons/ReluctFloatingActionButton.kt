package work.racka.reluct.compose.common.components.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun ReluctFloatingActionButton(
    buttonText: String,
    icon: ImageVector,
    contentDescription: String?,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = onButtonClicked,
            shape = Shapes.large,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .animateContentSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription
                )
                if (expanded) {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalContentColor.current
                    )
                }
            }
        }
    }
}
