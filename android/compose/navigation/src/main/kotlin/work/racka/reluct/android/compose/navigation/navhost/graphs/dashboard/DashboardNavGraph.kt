package work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.android.compose.navigation.destinations.Graphs
import work.racka.reluct.android.compose.navigation.destinations.graphs.DashboardDestinations

@ExperimentalAnimationApi
internal fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.DashboardDestinations.route,
        startDestination = DashboardDestinations.Overview.route
    ) {
        Timber.d("Dashboard screen called")
        // Overview
        composable(
            route = DashboardDestinations.Overview.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dashboard: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Statistics
        composable(
            route = DashboardDestinations.Statistics.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dashboard: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}