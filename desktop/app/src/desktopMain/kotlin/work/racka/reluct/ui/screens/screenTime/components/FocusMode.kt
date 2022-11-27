package work.racka.reluct.ui.screens.screenTime.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import work.racka.reluct.compose.common.components.cards.cardWithActions.ReluctDescriptionCard
import work.racka.reluct.compose.common.components.cards.cardWithActions.ReluctSwitchCard

@Composable
internal fun LimitsSwitchCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    ReluctSwitchCard(
        modifier = modifier,
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },
        contentColor = contentColor,
        containerColor = containerColor,
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
    title: String,
    description: String,
    modifier: Modifier = Modifier,
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
        leftItems = { icon?.let { Icon(imageVector = it, contentDescription = null) } },
        onClick = onClick,
        rightItems = {
            Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "Open")
        },
        bottomContent = bottomContent
    )
}
