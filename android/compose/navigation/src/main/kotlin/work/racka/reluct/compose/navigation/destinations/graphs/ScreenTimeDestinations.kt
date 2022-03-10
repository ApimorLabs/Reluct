package work.racka.reluct.compose.navigation.destinations.graphs

internal enum class ScreenTimeDestinations(
    val route: String,
    val label: String
) {
    Statistics(
        route = "ScreenTimeStatistics",
        label = "Statistics"
    ),
    Limits(
        route = "ScreenTimeLimits",
        label = "Limits"
    );

    enum class Paths(
        val route: String
    )
}