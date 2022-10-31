package work.racka.reluct.android.compose.navigation.navhost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import work.racka.reluct.android.compose.components.animations.slideInVerticallyFadeReversed
import work.racka.reluct.android.compose.components.animations.slideOutVerticallyFadeReversed
import work.racka.reluct.android.compose.components.util.rememberBarVisibility
import work.racka.reluct.android.compose.navigation.navbar.NavbarDestinations
import work.racka.reluct.android.compose.navigation.navbar.ReluctBottomNavBar
import work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard.DashboardNavHost
import work.racka.reluct.android.compose.navigation.navhost.graphs.extras.otherScreenNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.goals.GoalsNavHost
import work.racka.reluct.android.compose.navigation.navhost.graphs.goals.goalDataNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.screentime.ScreenTimeNavHost
import work.racka.reluct.android.compose.navigation.navhost.graphs.screentime.appScreenTimeStatsNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.tasks.TasksNavHost
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.SettingsCheck
import work.racka.reluct.android.screens.onboarding.OnBoardingScreen
import work.racka.reluct.android.screens.settings.SettingsScreen
import work.racka.reluct.common.core_navigation.compose_destinations.onboarding.OnBoardingDestination
import work.racka.reluct.common.core_navigation.compose_destinations.settings.SettingsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.PendingTasksDestination

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(settingsCheck: SettingsCheck?, modifier: Modifier = Modifier) {
    val navController = rememberAnimatedNavController()
    val barsVisibility = rememberBarVisibility(defaultBottomBar = false)

    // Route used specifically for checking if the On Boarding flow was shown and Signed In
    val checkingRoute = "dummy_route"

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = barsVisibility.bottomBar.isVisible,
                enter = slideInVerticallyFadeReversed(),
                exit = slideOutVerticallyFadeReversed()
            ) {
                ReluctBottomNavBar(
                    navController = navController,
                    graphStartDestination = NavbarDestinations.Dashboard.route
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        AnimatedNavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = checkingRoute,
            route = "root"
        ) {
            // Checking Route
            composable(
                route = checkingRoute,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }
            ) {
                LaunchedEffect(Unit, settingsCheck) {
                    settingsCheck?.let { check ->
                        if (check.isOnBoardingDone) {
                            navController.navigate(NavbarDestinations.Dashboard.route) {
                                popUpTo(checkingRoute) { inclusive = true }
                            }
                        } else {
                            navController.navigate(OnBoardingDestination.route) {
                                popUpTo(checkingRoute) { inclusive = true }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                SideEffect { barsVisibility.bottomBar.hide() }
            }

            // OnBoarding
            composable(
                route = OnBoardingDestination.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                OnBoardingScreen(
                    navigateHome = {
                        navController.navigate(NavbarDestinations.Dashboard.route) {
                            popUpTo(OnBoardingDestination.route) { inclusive = true }
                        }
                    }
                )

                SideEffect { barsVisibility.bottomBar.hide() }
            }

            // Dashboard
            composable(
                route = NavbarDestinations.Dashboard.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                DashboardNavHost(
                    mainNavController = navController,
                    barsVisibility = barsVisibility,
                    mainScaffoldPadding = innerPadding
                )
            }

            // Tasks
            composable(
                route = NavbarDestinations.Tasks.route,
                deepLinks = PendingTasksDestination.deepLinks,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                TasksNavHost(
                    mainNavController = navController,
                    mainScaffoldPadding = innerPadding,
                    barsVisibility = barsVisibility
                )
            }

            // Screen Time
            composable(
                route = NavbarDestinations.ScreenTime.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                ScreenTimeNavHost(
                    mainNavController = navController,
                    barsVisibility = barsVisibility,
                    mainScaffoldPadding = innerPadding
                )
            }

            // Goals
            composable(
                route = NavbarDestinations.Goals.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                GoalsNavHost(
                    mainNavController = navController,
                    barsVisibility = barsVisibility,
                    mainScaffoldPadding = innerPadding
                )
            }

            // Goal Data Graph
            goalDataNavGraph(
                navController = navController,
                barsVisibility = barsVisibility
            )

            // Settings
            composable(
                route = SettingsDestination.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                barsVisibility.bottomBar.hide()

                SettingsScreen(goBack = navController::popBackStack)
            }

            // Independent App Screen Time Stats and Limits Graph
            appScreenTimeStatsNavGraph(
                navController = navController,
                barsVisibility = barsVisibility
            )

            // All Other screens that don't share Scaffolds
            otherScreenNavGraph(
                navController = navController,
                barsVisibility = barsVisibility
            )
        }
    }
}
