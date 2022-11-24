package work.racka.reluct.android.navigation.navbar

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import work.racka.reluct.android.navigation.util.NavHelpers.navigateNavBarElements

@Composable
fun ReluctBottomNavBar(
    navController: NavHostController,
    graphStartDestination: String,
    modifier: Modifier = Modifier,
) {
    val items = NavbarDestinations.values()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = NavBarElevation.Level2
    ) {
        NavigationBar(
            modifier = modifier
                .navigationBarsPadding(),
            tonalElevation = NavBarElevation.Level0
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } ?: false

                NavigationBarItem(
                    label = {
                        Text(text = item.label, style = MaterialTheme.typography.labelMedium)
                    },
                    selected = selected,
                    // colors = ,
                    icon = {
                        Icon(
                            imageVector = if (selected) item.iconActive else item.iconInactive,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        navController.navigateNavBarElements(
                            route = item.route,
                            startDestination = graphStartDestination
                        )
                    }
                )
            }
        }
    }
}
