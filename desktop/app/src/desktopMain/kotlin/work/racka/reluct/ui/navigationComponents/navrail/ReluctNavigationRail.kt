package work.racka.reluct.ui.navigationComponents.navrail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Aod
import androidx.compose.material.icons.rounded.FactCheck
import androidx.compose.material.icons.rounded.Home
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

    val isActive by remember {
        derivedStateOf {
            currentConfig.value !is AppNavConfig.Checking &&
                currentConfig.value !is AppNavConfig.OnBoarding
            // && currentConfig.value !is AppNavConfig.Settings
        }
    }

    NavigationRail(modifier = modifier) {
        pages.forEach { page ->
            val selected by remember {
                derivedStateOf { currentConfig.value::class == page.config::class }
            }

            NavigationRailItem(
                modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                enabled = isActive,
                colors = getNavRailColors(isActive),
                selected = selected,
                label = { Text(text = page.label) },
                icon = {
                    Icon(
                        imageVector = if (selected) page.iconActive else page.iconInactive,
                        contentDescription = page.description
                    )
                },
                onClick = { onUpdateConfig(page.config) }
            )

            Spacer(Modifier.height(Dimens.ExtraSmallPadding.size))
        }
    }
}

@Composable
private fun getNavRailColors(isActive: Boolean) = NavigationRailItemDefaults
    .colors(
        unselectedIconColor = if (isActive) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = .5f)
        },
        unselectedTextColor = if (isActive) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = .5f)
        }
    )

private enum class RootNavDestinations(
    val config: AppNavConfig,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
    val label: String,
    val description: String? = null,
) {
    Dashboard(
        config = AppNavConfig.Dashboard,
        iconActive = Icons.Rounded.Home,
        iconInactive = Icons.Outlined.Home,
        label = "Home",
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
        label = "Usage",
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
