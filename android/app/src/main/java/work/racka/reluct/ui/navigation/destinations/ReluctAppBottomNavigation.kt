package work.racka.reluct.ui.navigation.destinations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.ui.graphics.vector.ImageVector

enum class ReluctAppBottomNavigation(
    val icon: ImageVector,
    val label: String
) {
    Home(
        icon = Icons.Default.Home,
        label = "Home"
    ),
    Summary(
        icon = Icons.Default.Summarize,
        label = "Summary"
    ),
}
