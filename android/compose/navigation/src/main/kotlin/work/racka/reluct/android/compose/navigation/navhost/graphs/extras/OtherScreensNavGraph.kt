package work.racka.reluct.android.compose.navigation.navhost.graphs.extras

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.compose.destinations.OtherDestinations
import work.racka.reluct.android.compose.destinations.navbar.Graphs
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavArgKeys
import work.racka.reluct.android.compose.navigation.util.NavHelpers
import work.racka.reluct.android.screens.tasks.add_edit.AddEditTaskScreen
import work.racka.reluct.android.screens.tasks.details.TaskDetailsScreen

@ExperimentalAnimationApi
fun NavGraphBuilder.otherScreenNavGraph(
    navController: NavHostController,
    updateNavBar: (hideNavBar: Boolean) -> Unit,
) {

    navigation(
        route = Graphs.OtherDestinations.route,
        startDestination = OtherDestinations.AddEditTask.route
    ) {
        // Add Task
        composable(
            route = "${OtherDestinations.AddEditTask.route}/{${NavArgKeys.ADD_EDIT_TASK_ID}}",
            arguments = listOf(
                navArgument(name = NavArgKeys.ADD_EDIT_TASK_ID) { type = NavType.StringType }
            ),
            enterTransition = {
                scaleInEnterTransition()
            },
            exitTransition = {
                scaleOutExitTransition()
            },
            popEnterTransition = {
                scaleInPopEnterTransition()
            },
            popExitTransition = {
                scaleOutPopExitTransition()
            }
        ) { navBackStackEntry ->
            updateNavBar(true)
            AddEditTaskScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, NavArgKeys.ADD_EDIT_TASK_ID),
                onBackClicked = { navController.popBackStack() }
            )
        }

        // Task Details
        composable(
            route = "${OtherDestinations.TaskDetails.route}/{${NavArgKeys.TASK_DETAILS_ID}}",
            arguments = listOf(
                navArgument(name = NavArgKeys.TASK_DETAILS_ID) { type = NavType.StringType }
            ),
            enterTransition = {
                scaleInEnterTransition()
            },
            exitTransition = {
                scaleOutExitTransition()
            },
            popEnterTransition = {
                scaleInPopEnterTransition()
            },
            popExitTransition = {
                scaleOutPopExitTransition()
            }
        ) { navBackStackEntry ->
            updateNavBar(true)
            TaskDetailsScreen(
                taskId = NavHelpers.getStringArgs(navBackStackEntry, NavArgKeys.TASK_DETAILS_ID),
                onNavigateToEditTask = {
                    navController.navigate(
                        "${OtherDestinations.AddEditTask.route}/$it"
                    )
                },
                onBackClicked = { navController.popBackStack() }
            )
        }

        // Task Search
        composable(
            route = OtherDestinations.SearchTasks.route,
            enterTransition = {
                scaleInEnterTransition()
            },
            exitTransition = {
                scaleOutExitTransition()
            },
            popEnterTransition = {
                scaleInPopEnterTransition()
            },
            popExitTransition = {
                scaleOutPopExitTransition()
            }
        ) {
            updateNavBar(true)
        }
    }
}