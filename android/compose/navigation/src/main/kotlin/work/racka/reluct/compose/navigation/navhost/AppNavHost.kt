package work.racka.reluct.compose.navigation.navhost

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import timber.log.Timber
import work.racka.reluct.compose.navigation.destinations.Graphs
import work.racka.reluct.compose.navigation.navbar.ReluctBottomNavBar
import work.racka.reluct.compose.navigation.navhost.graphs.dashboard.dashboardNavGraph
import work.racka.reluct.compose.navigation.navhost.graphs.goals.goalsNavGraph
import work.racka.reluct.compose.navigation.navhost.graphs.screentime.screenTimeNavGraph
import work.racka.reluct.compose.navigation.navhost.graphs.tasks.tasksNavGraph

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController()
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            ReluctBottomNavBar(navController = navController)
        }
    ) { innerPadding ->

        AnimatedNavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = Graphs.DashboardDestinations.route,
            route = Graphs.RootDestinations.route
        ) {
            Timber.d("Enter this navHost")

            // Dashboard
            dashboardNavGraph(navController = navController)

            // Tasks
            tasksNavGraph(navController)

            // Screen Time
            screenTimeNavGraph(navController)

            // Goals
            goalsNavGraph(navController)
        }
    }

}