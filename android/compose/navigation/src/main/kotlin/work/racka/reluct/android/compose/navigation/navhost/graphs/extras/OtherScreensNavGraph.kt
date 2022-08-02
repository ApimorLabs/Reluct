package work.racka.reluct.android.compose.navigation.navhost.graphs.extras

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.navigation.destinations.OtherDestination
import work.racka.reluct.android.compose.navigation.destinations.screentime.AppScreenTimeStatsArgs
import work.racka.reluct.android.compose.navigation.destinations.screentime.AppScreenTimeStatsDestination
import work.racka.reluct.android.compose.navigation.destinations.tasks.*
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavHelpers
import work.racka.reluct.android.compose.navigation.util.NavHelpers.popBackStackOrExitActivity
import work.racka.reluct.android.screens.screentime.app_stats_details.AppScreenTimeStatsScreen
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

            // Is safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            AddEditTaskScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, AddEditTaskArgs.TaskId.name),
                onBackClicked = { navController.popBackStackOrExitActivity(activity) }
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

            // Is safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            TaskDetailsScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, TaskDetailsArgs.TaskId.name),
                onNavigateToEditTask = {
                    navController.navigate(
                        AddEditTaskDestination.argsRoute(it)
                    )
                },
                onBackClicked = { navController.popBackStackOrExitActivity(activity) }
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

        // App Screen Time Stats
        composable(
            route = AppScreenTimeStatsDestination.route,
            arguments = AppScreenTimeStatsDestination.args,
            deepLinks = AppScreenTimeStatsDestination.deepLinks,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->
            barsVisibility.bottomBar.hide()

            // Is safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            AppScreenTimeStatsScreen(
                packageName = NavHelpers
                    .getStringArgs(backStackEntry, AppScreenTimeStatsArgs.PackageName.name) ?: "",
                onBackClicked = { navController.popBackStackOrExitActivity(activity) }
            )
        }
    }
}