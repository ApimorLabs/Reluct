package work.racka.reluct.android.screens.screentime.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctDescriptionCard
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctSwitchCard

@Composable
internal fun LimitsSwitchCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit = {},
) {
    ReluctSwitchCard(
        modifier = modifier,
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },
        icon = icon,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current
            )
        },
        description = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
        },
        onClick = onClick
    )
}

@Composable
fun LimitsDetailsCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    bottomContent: @Composable ColumnScope.() -> Unit = {}
) {
    ReluctDescriptionCard(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current
            )
        },
        description = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
        },
        icon = icon,
        onClick = onClick,
        rightActions = {
            Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "Open")
        },
        bottomContent = bottomContent
    )
}