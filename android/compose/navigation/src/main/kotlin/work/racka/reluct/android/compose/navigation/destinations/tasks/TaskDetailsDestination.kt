package work.racka.reluct.android.compose.navigation.destinations.tasks

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import work.racka.reluct.common.core_navigation.destination.NavDestination

object TaskDetailsDestination : NavDestination {
    private const val BASE_URI = "task_details"
    override val route: String = "$BASE_URI-route/{${AddEditTaskArgs.TaskId.name}}"
    override val destination: String = "$BASE_URI-destination"

    override val args: List<NamedNavArgument>
        get() = listOf(
            navArgument(AddEditTaskArgs.TaskId.name) {
                type = NavType.StringType
                defaultValue = null
            }
        )

    fun argsRoute(taskId: String?) = "$BASE_URI-route/$taskId"
}

enum class TaskDetailsArgs {
    TaskId;
}