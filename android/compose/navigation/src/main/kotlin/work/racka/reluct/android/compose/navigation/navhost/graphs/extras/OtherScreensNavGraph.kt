package work.racka.reluct.android.compose.navigation.navhost.graphs.extras

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.navigation.destinations.OtherDestination
import work.racka.reluct.android.compose.navigation.destinations.tasks.*
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavHelpers
import work.racka.reluct.android.screens.tasks.add_edit.AddEditTaskScreen
import work.racka.reluct.android.screens.tasks.details.TaskDetailsScreen
import work.racka.reluct.android.screens.tasks.search.TasksSearchScreen

@ExperimentalAnimationApi
fun NavGraphBuilder.otherScreenNavGraph(
    navController: NavHostController,
    barsVisibility: BarsVisibility
) {
    navigation(
        route = OtherDestination.route,
        startDestination = AddEditTaskDestination.route
    ) {
        // Add Task
        composable(
            route = AddEditTaskDestination.route,
            arguments = AddEditTaskDestination.args,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { navBackStackEntry ->
            barsVisibility.bottomBar.hide()
            AddEditTaskScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, AddEditTaskArgs.TaskId.name),
                onBackClicked = { navController.popBackStack() }
            )
        }

        // Task Details
        composable(
            route = TaskDetailsDestination.route,
            arguments = TaskDetailsDestination.args,
            deepLinks = TaskDetailsDestination.deepLinks,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { navBackStackEntry ->
            barsVisibility.bottomBar.hide()
            TaskDetailsScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, TaskDetailsArgs.TaskId.name),
                onNavigateToEditTask = {
                    navController.navigate(
                        AddEditTaskDestination.argsRoute(it)
                    )
                },
                onBackClicked = { navController.popBackStack() }
            )
        }

        // Task Search
        composable(
            route = SearchTasksDestination.route,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) {
            barsVisibility.bottomBar.hide()
            TasksSearchScreen(
                onNavigateToTaskDetails = {
                    navController.navigate(
                        TaskDetailsDestination.argsRoute(it)
                    )
                },
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}