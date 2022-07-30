package work.racka.reluct.android.compose.components.cards.card_with_actions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReluctSwitchCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit = {}
) {

    Card(
        enabled = enabled,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth() then modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            icon?.let {
                Icon(imageVector = icon, contentDescription = null)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.Start
            ) {
                title()
                description()
            }

            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) },
                enabled = enabled
            )
        }
        bottomContent()
    }
}