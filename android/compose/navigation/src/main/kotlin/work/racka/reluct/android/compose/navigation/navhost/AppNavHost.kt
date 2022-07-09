package work.racka.reluct.android.compose.navigation.navhost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import work.racka.reluct.android.compose.components.util.rememberBarVisibility
import work.racka.reluct.android.compose.components.util.slideInVerticallyFadeReversed
import work.racka.reluct.android.compose.components.util.slideOutVerticallyFadeReversed
import work.racka.reluct.android.compose.navigation.navbar.NavbarDestinations
import work.racka.reluct.android.compose.navigation.navbar.ReluctBottomNavBar
import work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard.dashboardNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.extras.otherScreenNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.goals.goalsNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.screentime.ScreenTimeNavHost
import work.racka.reluct.android.compose.navigation.navhost.graphs.tasks.TasksNavHost

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(modifier: Modifier = Modifier) {

    val navController = rememberAnimatedNavController()
    val barsVisibility = rememberBarVisibility()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = barsVisibility.bottomBar.isVisible,
                enter = slideInVerticallyFadeReversed(),
                exit = slideOutVerticallyFadeReversed()
            ) {
                ReluctBottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->

        AnimatedNavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = NavbarDestinations.Dashboard.route,
            route = "root"
        ) {

            // Dashboard
            dashboardNavGraph(
                navController = navController
            )

            // Tasks
            composable(
                route = NavbarDestinations.Tasks.route
            ) {
                TasksNavHost(
                    mainNavController = navController,
                    mainScaffoldPadding = innerPadding,
                    barsVisibility = barsVisibility
                )
            }

            // Screen Time
            composable(
                route = NavbarDestinations.ScreenTime.route
            ) {
                ScreenTimeNavHost(
                    mainNavController = navController,
                    barsVisibility = barsVisibility,
                    mainScaffoldPadding = innerPadding
                )
            }

            // Goals
            goalsNavGraph(navController)

            //All Other screens that don't share Scaffolds
            otherScreenNavGraph(
                navController = navController,
                barsVisibility = barsVisibility
            )
        }
    }

}