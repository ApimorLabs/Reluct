package work.racka.reluct.ui.navigation.navhost

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.ui.main.screens.dashboard.DashboardScreen
import work.racka.reluct.ui.navigation.destinations.ReluctAppBottomNavigation

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun ReluctAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    Scaffold(
        modifier = modifier,
        bottomBar = { }
    ) { innerPadding ->

        AnimatedNavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = ReluctAppBottomNavigation.Home.name,
            route = "MainNavHost"
        ) {
            Timber.d("Enter this navHost")

            // Home
            composable(
                route = ReluctAppBottomNavigation.Home.name,
            ) {
                Timber.d("Dashboard screen called")
                DashboardScreen()
            }

            // Summary
            composable(
                route = ReluctAppBottomNavigation.Summary.name,
            ) {
                Timber.d("Summary Called")

            }

        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController
) {

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        ReluctAppBottomNavigation.values().forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = LocalContentColor.current.copy(ContentAlpha.disabled),
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(text = screen.label)
                },
                selected = currentDestination?.hierarchy?.any {
                    Timber.d("Selected: ${screen.label} -> ${it.route == screen.name}")
                    it.route == screen.name
                } == true,
                onClick = {
                    Timber.d("NavBar item clicked")
                    navController.navigate(screen.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

}
