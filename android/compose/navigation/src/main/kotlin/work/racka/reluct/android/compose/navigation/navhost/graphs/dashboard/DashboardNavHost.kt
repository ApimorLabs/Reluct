package work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import timber.log.Timber
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.topBar.ReluctPageHeading
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.navigation.navbar.NavbarDestinations
import work.racka.reluct.android.compose.navigation.toptabs.dashboard.DashboardTabBar
import work.racka.reluct.android.compose.navigation.toptabs.dashboard.DashboardTabDestination
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavHelpers.navigateNavBarElements
import work.racka.reluct.android.screens.dashboard.overview.DashboardOverviewScreen
import work.racka.reluct.android.screens.dashboard.statistics.DashboardStatsScreen
import work.racka.reluct.common.core_navigation.compose_destinations.dashboard.DashboardOverviewDestination
import work.racka.reluct.common.core_navigation.compose_destinations.dashboard.DashboardStatsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.GoalDetailsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.screentime.AppScreenTimeStatsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.settings.SettingsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.TaskDetailsDestination

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun DashboardNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    mainScaffoldPadding: PaddingValues,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage = remember(currentDestination) {
        derivedStateOf {
            getCurrentTab(currentDestination)
        }
    }

    Scaffold(
        topBar = {
            DashboardScreenTopBar(
                tabPage = tabPage,
                // profilePicUrl = "https://pbs.twimg.com/profile_images/1451052243067805698/LIEt076e_400x400.jpg",
                updateTabPage = {
                    navController.navigateNavBarElements(it.route)
                },
                onSettingsClicked = { mainNavController.navigate(SettingsDestination.route) }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
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
                Timber.d("Dashboard Overview Screen")
                DashboardOverviewScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToScreenTime = {
                        mainNavController.navigateNavBarElements(
                            route = NavbarDestinations.ScreenTime.route
                        )
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            TaskDetailsDestination.argsRoute(it)
                        )
                    },
                    onNavigateToGoalDetails = { goalId: String? ->
                        mainNavController.navigate(GoalDetailsDestination.argsRoute(goalId))
                    }
                )
            }

            // Dashboard Stats
            composable(
                route = DashboardStatsDestination.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                DashboardStatsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAppUsageInfo = { packageName ->
                        mainNavController.navigate(
                            AppScreenTimeStatsDestination.argsRoute(packageName)
                        )
                    },
                    onNavigateToScreenTimeStats = {
                        mainNavController.navigateNavBarElements(
                            route = NavbarDestinations.ScreenTime.route
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DashboardScreenTopBar(
    tabPage: State<DashboardTabDestination>,
    // profilePicUrl: String?,
    updateTabPage: (DashboardTabDestination) -> Unit,
    onSettingsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        ReluctPageHeading(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(id = R.string.dashboard_destination_text),
            extraItems = {
                IconButton(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    ),
                    onClick = onSettingsClicked,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings"
                    )
                }
                /*ProfilePicture(
                    modifier = Modifier,//.padding(4.dp),
                    pictureUrl = profilePicUrl,
                    size = 36.dp
                )*/
            }
        )

        LazyRow {
            item {
                DashboardTabBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
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
