package work.racka.reluct.ui.navigationComponents.navrail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Aod
import androidx.compose.material.icons.rounded.FactCheck
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import work.racka.reluct.common.core.navigation.destination.AppNavConfig
import work.racka.reluct.compose.common.theme.Dimens

private const val NAV_RAIL_ELEVATION = 2.0

@Composable
fun ReluctNavigationRail(
    currentConfig: State<AppNavConfig>,
    onUpdateConfig: (AppNavConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = RootNavDestinations.values()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = NAV_RAIL_ELEVATION.dp
    ) {
        NavigationRail(
            modifier = modifier.padding(Dimens.SmallPadding.size)
        ) {
            pages.forEach { page ->
                val selected by remember {
                    derivedStateOf { currentConfig.value::class == page.config::class }
                }
                NavigationRailItem(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                    selected = selected,
                    label = {
                        Text(text = page.label, style = MaterialTheme.typography.labelMedium)
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) page.iconActive else page.iconInactive,
                            contentDescription = page.description
                        )
                    },
                    onClick = { onUpdateConfig(page.config) }
                )

                Spacer(Modifier.height(Dimens.SmallPadding.size))
            }
        }
    }
}

private enum class RootNavDestinations(
    val config: AppNavConfig,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
    val label: String,
    val description: String? = null,
) {
    Dashboard(
        config = AppNavConfig.Dashboard,
        iconActive = Icons.Rounded.GridView,
        iconInactive = Icons.Outlined.GridView,
        label = "Dashboard",
        description = "Open Dashboard"
    ),
    Tasks(
        config = AppNavConfig.Tasks(),
        iconActive = Icons.Rounded.FactCheck,
        iconInactive = Icons.Outlined.FactCheck,
        label = "Tasks",
        description = "Open Tasks"
    ),
    ScreenTime(
        config = AppNavConfig.ScreenTime(),
        iconActive = Icons.Rounded.Aod,
        iconInactive = Icons.Outlined.Aod,
        label = "Screen Time",
        description = "Open Screen Time"
    ),
    Goals(
        config = AppNavConfig.Goals(),
        iconActive = Icons.Rounded.TaskAlt,
        iconInactive = Icons.Outlined.TaskAlt,
        label = "Goals",
        description = "Open Goals"
    );
}
