package work.racka.reluct.common.core_navigation.compose_destinations.tasks

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import work.racka.reluct.common.core_navigation.destination.NavDestination

object AddEditTaskDestination : NavDestination {
    private const val BASE_URI = "add_edit_task"
    override val route: String = "$BASE_URI-route/{${AddEditTaskArgs.TaskId.name}}"
    override val destination: String = "$BASE_URI-destination"

    override val args: List<NamedNavArgument>
        get() = listOf(
            navArgument(AddEditTaskArgs.TaskId.name) {
                type = NavType.StringType
            }
        )

    fun argsRoute(taskId: String?) = "$BASE_URI-route/$taskId"
}

enum class AddEditTaskArgs {
    TaskId;
}