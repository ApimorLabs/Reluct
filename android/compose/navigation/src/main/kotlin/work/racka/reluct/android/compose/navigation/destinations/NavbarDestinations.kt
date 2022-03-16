package work.racka.reluct.android.compose.navigation.destinations

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

internal enum class NavbarDestinations(
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
    val label: String,
    val description: String? = null
) {
    Dashboard(
        iconActive = Icons.Rounded.GridView,
        iconInactive = Icons.Outlined.GridView,
        label = "Dashboard",
        description = "Open Dashboard"
    ),
    Tasks(
        iconActive = Icons.Rounded.FactCheck,
        iconInactive = Icons.Outlined.FactCheck,
        label = "Tasks",
        description = "Open Tasks"
    ),
    ScreenTime(
        iconActive = Icons.Rounded.Aod,
        iconInactive = Icons.Outlined.Aod,
        label = "Screen Time",
        description = "Open Screen Time"
    ),
    Goals(
        iconActive = Icons.Rounded.TaskAlt,
        iconInactive = Icons.Outlined.TaskAlt,
        label = "Goals",
        description = "Open Goals"
    );

}