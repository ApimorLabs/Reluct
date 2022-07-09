package work.racka.reluct.android.compose.navigation.navhost.graphs.screentime

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.destinations.ScreenTimeDestinations
import work.racka.reluct.android.compose.destinations.navbar.Graphs
import work.racka.reluct.android.compose.navigation.top_tabs.screentime.ScreenTimeTabBar
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.screens.screentime.statistics.ScreenTimeStatisticsScreen

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun ScreenTimeNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    navController: NavHostController = rememberAnimatedNavController(),
    mainScaffoldPadding: PaddingValues,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage by remember(currentDestination) {
        derivedStateOf {
            getCurrentTab(currentDestination)
        }
    }

    Scaffold(
        topBar = {
            ScreenTimeTopBar(
                tabPage = tabPage,
                profilePicUrl = "https://pbs.twimg.com/profile_images/1451052243067805698/LIEt076e_400x400.jpg",
                updateTabPage = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    ) { innerPapping ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPapping),
            navController = navController,
            route = Graphs.RootDestinations.route + Graphs.ScreenTimeDestinations.route,
            startDestination = ScreenTimeDestinations.Statistics.route
        ) {
            // Statistics
            composable(
                route = ScreenTimeDestinations.Statistics.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                ScreenTimeStatisticsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAppUsageInfo = { /* TODO: Setup App Info Screen */ }
                )
            }

            // Limits
            composable(
                route = ScreenTimeDestinations.Limits.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Screen Time: $route",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenTimeTopBar(
    tabPage: ScreenTimeDestinations,
    profilePicUrl: String?,
    updateTabPage: (ScreenTimeDestinations) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        // TODO: Put Prof Pic and Destination Name

        LazyRow {
            item {
                ScreenTimeTabBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    tabPage = tabPage,
                    onTabSelected = {
                        updateTabPage(it)
                    }
                )
            }
        }
    }
}

private fun getCurrentTab(currentDestination: NavDestination?): ScreenTimeDestinations {
    val destinations = ScreenTimeDestinations.values()
    destinations.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any {
            it.route == item.route
        } ?: false
        if (isSelected) return item
    }
    return destinations.first()
}