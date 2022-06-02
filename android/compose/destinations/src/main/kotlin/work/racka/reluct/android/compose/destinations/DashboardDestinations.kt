package work.racka.reluct.android.compose.destinations

enum class DashboardDestinations(
    val route: String,
    val label: String
) {
    Overview(
        route = "DashboardOverview",
        label = "Overview"
    ),
    Statistics(
        route = "DashboardStatistics",
        label = "Statistics"
    );

    enum class Paths(
        val route: String
    )
}