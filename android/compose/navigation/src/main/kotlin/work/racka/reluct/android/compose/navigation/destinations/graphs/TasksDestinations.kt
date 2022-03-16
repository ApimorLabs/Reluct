package work.racka.reluct.android.compose.navigation.destinations.graphs

internal enum class TasksDestinations(
    val route: String,
    val label: String
) {
    Tasks(
        route = "TasksTasks",
        label = "Tasks"
    ),
    Done(
        route = "TasksDone",
        label = "Done"
    ),
    Statistics(
        route = "TasksStatistics",
        label = "Statistics"
    );

    enum class Paths(
        val route: String
    )
}