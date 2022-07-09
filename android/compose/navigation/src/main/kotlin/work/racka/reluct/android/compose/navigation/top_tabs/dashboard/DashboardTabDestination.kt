package work.racka.reluct.android.compose.navigation.top_tabs.dashboard

enum class DashboardTabDestination(
    val route: String
) {
    Overview(
        route = "DashboardOverview"
    ),
    Statistics(
        route = "DashboardStatistics"
    );
}