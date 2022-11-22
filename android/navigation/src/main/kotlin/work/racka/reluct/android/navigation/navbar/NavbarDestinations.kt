package work.racka.reluct.android.navigation.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Aod
import androidx.compose.material.icons.rounded.FactCheck
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardOverviewDestination
import work.racka.reluct.common.core.navigation.composeDestinations.goals.ActiveGoalsDestination
import work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeStatsDestination
import work.racka.reluct.common.core.navigation.composeDestinations.tasks.PendingTasksDestination

enum class NavbarDestinations(
    val route: String,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
    val label: String,
    val description: String? = null,
) {
    Dashboard(
        route = work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardOverviewDestination.destination,
        iconActive = Icons.Rounded.GridView,
        iconInactive = Icons.Outlined.GridView,
        label = "Dashboard",
        description = "Open Dashboard"
    ),
    Tasks(
        route = work.racka.reluct.common.core.navigation.composeDestinations.tasks.PendingTasksDestination.destination,
        iconActive = Icons.Rounded.FactCheck,
        iconInactive = Icons.Outlined.FactCheck,
        label = "Tasks",
        description = "Open Tasks"
    ),
    ScreenTime(
        route = work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeStatsDestination.destination,
        iconActive = Icons.Rounded.Aod,
        iconInactive = Icons.Outlined.Aod,
        label = "Screen Time",
        description = "Open Screen Time"
    ),
    Goals(
        route = work.racka.reluct.common.core.navigation.composeDestinations.goals.ActiveGoalsDestination.destination,
        iconActive = Icons.Rounded.TaskAlt,
        iconInactive = Icons.Outlined.TaskAlt,
        label = "Goals",
        description = "Open Goals"
    );
}
