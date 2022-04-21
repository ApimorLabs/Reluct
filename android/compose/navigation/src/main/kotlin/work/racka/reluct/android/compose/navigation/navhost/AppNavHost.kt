package work.racka.reluct.android.compose.navigation.navhost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import work.racka.reluct.android.compose.components.navbar.ReluctBottomNavBar
import work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard.dashboardNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.extras.otherScreenNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.goals.goalsNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.screentime.screenTimeNavGraph
import work.racka.reluct.android.compose.navigation.navhost.graphs.tasks.TasksNavHost
import work.racka.reluct.common.compose.destinations.navbar.Graphs

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val hideNavBar = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = !hideNavBar.value,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing),
                    initialOffsetY = { fullHeight -> fullHeight }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing),
                    targetOffsetY = { fullHeight -> fullHeight }
                )
            ) {
                ReluctBottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->

        AnimatedNavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = Graphs.DashboardDestinations.route,
            route = Graphs.RootDestinations.route
        ) {

            // Dashboard
            dashboardNavGraph(
                navController = navController
            )

            // Tasks
            composable(
                route = Graphs.TasksDestinations.route
            ) {
                hideNavBar.value = false
                TasksNavHost(
                    mainNavController = navController
                )
            }

            // Screen Time
            screenTimeNavGraph(navController)

            // Goals
            goalsNavGraph(navController)

            //All Other screens that don't share Scaffolds
            otherScreenNavGraph(
                navController = navController,
                updateNavBar = { hideNavBar.value = it }
            )
        }
    }

}