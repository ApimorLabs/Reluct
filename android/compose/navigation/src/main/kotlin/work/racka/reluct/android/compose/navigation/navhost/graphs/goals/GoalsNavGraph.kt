package work.racka.reluct.android.compose.navigation.navhost.graphs.goals

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
import work.racka.reluct.common.compose.destinations.GoalsDestinations

@ExperimentalAnimationApi
internal fun NavGraphBuilder.goalsNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.GoalsDestinations.route,
        startDestination = GoalsDestinations.Ongoing.route
    ) {
        Timber.d("Goals screen called")
        // Ongoing
        composable(
            route = GoalsDestinations.Ongoing.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Goals: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Completed
        composable(
            route = GoalsDestinations.Completed.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Goals: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}