package work.racka.reluct.android.compose.navigation.navhost.graphs.extras

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavHelpers
import work.racka.reluct.android.compose.navigation.util.NavHelpers.popBackStackOrExitActivity
import work.racka.reluct.android.screens.tasks.add_edit.AddEditTaskScreen
import work.racka.reluct.android.screens.tasks.details.TaskDetailsScreen
import work.racka.reluct.android.screens.tasks.search.TasksSearchScreen
import work.racka.reluct.common.core_navigation.compose_destinations.OtherDestination
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.*

@ExperimentalAnimationApi
fun NavGraphBuilder.otherScreenNavGraph(
    navController: NavHostController,
    barsVisibility: BarsVisibility
) {
    navigation(
        route = OtherDestination.route,
        startDestination = TaskDetailsDestination.route
    ) {
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
            // Its safe to cast since this composable will always be inside an activity
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

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Add Task
        composable(
            route = AddEditTaskDestination.route,
            arguments = AddEditTaskDestination.args,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { navBackStackEntry ->
            // Its safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            AddEditTaskScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, AddEditTaskArgs.TaskId.name),
                onBackClicked = { navController.popBackStackOrExitActivity(activity) }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Task Search
        composable(
            route = SearchTasksDestination.route,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) {
            TasksSearchScreen(
                onNavigateToTaskDetails = {
                    navController.navigate(
                        TaskDetailsDestination.argsRoute(it)
                    )
                },
                onBackClicked = { navController.popBackStack() }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }
    }
}
