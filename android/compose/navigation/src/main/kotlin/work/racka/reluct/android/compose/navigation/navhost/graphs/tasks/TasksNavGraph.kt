package work.racka.reluct.android.compose.navigation.navhost.graphs.tasks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.android.compose.components.tab.tasks.TasksTabBar
import work.racka.reluct.android.compose.navigation.destinations.Graphs
import work.racka.reluct.common.compose.destinations.TasksDestinations

@OptIn(ExperimentalMaterial3Api::class)
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
            val tabPage = remember {
                mutableStateOf(TasksDestinations.Tasks)
            }
            Scaffold(
                topBar = {
                    LazyRow {
                        item {
                            TasksTabBar(
                                modifier = Modifier
                                    .statusBarsPadding(),
                                tabPage = tabPage.value,
                                onTabSelected = { tabPage.value = it }
                            )
                        }
                    }
                }
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