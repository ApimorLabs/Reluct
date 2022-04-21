package work.racka.reluct.android.compose.navigation.navhost.graphs.extras

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.screens.tasks.add_edit.AddEditTaskScreen
import work.racka.reluct.common.compose.destinations.OtherDestinations
import work.racka.reluct.common.compose.destinations.TasksDestinations
import work.racka.reluct.common.compose.destinations.navbar.Graphs

@OptIn(ExperimentalMaterial3Api::class)
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
            route = "${TasksDestinations.Paths.AddEditTask.route}/{EditTask}",
            arguments = listOf(
                navArgument(name = "EditTask") { type = NavType.StringType }
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
                taskId = navBackStackEntry.arguments?.getString("EditTask"),
                onBackClicked = {
                    navController.popBackStack()
                    navBackStackEntry.arguments?.remove("EditTask")
                }
            )
        }
    }
}