package work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import work.racka.reluct.android.compose.navigation.navbar.NavbarDestinations
import work.racka.reluct.android.compose.navigation.top_tabs.dashboard.DashboardTabBar
import work.racka.reluct.android.compose.navigation.top_tabs.dashboard.DashboardTabDestination
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.screens.dashboard.overview.DashboardOverviewScreen
import work.racka.reluct.common.core_navigation.compose_destinations.dashboard.DashboardOverviewDestination
import work.racka.reluct.common.core_navigation.compose_destinations.dashboard.DashboardStatsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.TaskDetailsDestination

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun DashboardNavHost(
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
            DashboardScreenTopBar(
                tabPage = tabPage,
                profilePicUrl = null,
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
    ) { innerPadding ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            route = DashboardOverviewDestination.destination,
            startDestination = DashboardOverviewDestination.route
        ) {
            // Overview
            composable(
                route = DashboardOverviewDestination.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                DashboardOverviewScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToScreenTime = {
                        mainNavController.navigate(NavbarDestinations.ScreenTime.route)
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            TaskDetailsDestination.argsRoute(it)
                        )
                    }
                )
            }

            composable(
                route = DashboardStatsDestination.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun DashboardScreenTopBar(
    tabPage: DashboardTabDestination,
    profilePicUrl: String?,
    updateTabPage: (DashboardTabDestination) -> Unit,
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
                DashboardTabBar(
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

private fun getCurrentTab(currentDestination: NavDestination?): DashboardTabDestination {
    val destinations = DashboardTabDestination.values()
    destinations.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any {
            it.route == item.route
        } ?: false
        if (isSelected) return item
    }
    return destinations.first()
}