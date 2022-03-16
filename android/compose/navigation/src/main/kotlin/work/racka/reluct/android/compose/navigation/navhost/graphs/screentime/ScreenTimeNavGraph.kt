package work.racka.reluct.android.compose.navigation.navhost.graphs.screentime

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
import work.racka.reluct.android.compose.navigation.destinations.graphs.ScreenTimeDestinations

@ExperimentalAnimationApi
internal fun NavGraphBuilder.screenTimeNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.ScreenTimeDestinations.route,
        startDestination = ScreenTimeDestinations.Statistics.route
    ) {
        Timber.d("Statistics screen called")
        // Statistics
        composable(
            route = ScreenTimeDestinations.Statistics.route
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

        // Limits
        composable(
            route = ScreenTimeDestinations.Limits.route
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