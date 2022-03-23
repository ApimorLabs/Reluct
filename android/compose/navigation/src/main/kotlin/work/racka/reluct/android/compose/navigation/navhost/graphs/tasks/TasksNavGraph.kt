package work.racka.reluct.android.compose.navigation.navhost.graphs.tasks

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
import work.racka.reluct.common.compose.destinations.TasksDestinations

@ExperimentalAnimationApi
internal fun NavGraphBuilder.tasksNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.TasksDestinations.route,
        startDestination = TasksDestinations.Tasks.route
    ) {
        Timber.d("Tasks screen called")

        // Tasks
        composable(
            route = TasksDestinations.Tasks.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tasks: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Done
        composable(
            route = TasksDestinations.Done.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tasks: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        // Statistics
        composable(
            route = TasksDestinations.Statistics.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tasks: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}