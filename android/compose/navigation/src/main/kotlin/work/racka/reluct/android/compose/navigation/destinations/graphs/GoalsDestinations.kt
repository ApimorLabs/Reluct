package work.racka.reluct.android.compose.navigation.destinations.graphs

internal enum class GoalsDestinations(
    val route: String,
    val label: String
) {
    Ongoing(
        route = "GoalsOngoing",
        label = "Ongoing"
    ),
    Completed(
        route = "GoalsCompleted",
        label = "Completed"
    );

    enum class Paths(
        val route: String
    )
}