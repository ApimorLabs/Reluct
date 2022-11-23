package work.racka.reluct.ui.navigationComponents.navrail

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import work.racka.reluct.common.core.navigation.destination.AppNavConfig

private const val NAV_RAIL_ELEVATION_L3 = 3.0

@Composable
fun ReluctNavigationRail(
    currentConfig: State<AppNavConfig>,
    onUpdateConfig: (AppNavConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = RootNavDestinations.values()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = NAV_RAIL_ELEVATION_L3.dp
    ) {
        NavigationRail(
            modifier = modifier
        ) {
            pages.forEach { page ->
                val selected by remember { derivedStateOf { page.config == currentConfig.value } }
                NavigationRailItem(
                    selected = selected,
                    // colors = ,
                    icon = {
                        Icon(
                            imageVector = if (selected) page.iconActive else page.iconInactive,
                            contentDescription = page.description
                        )
                    },
                    onClick = { onUpdateConfig(page.config) }
                )
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
