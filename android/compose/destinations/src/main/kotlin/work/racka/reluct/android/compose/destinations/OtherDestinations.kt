package work.racka.reluct.android.compose.destinations

enum class OtherDestinations(
    val route: String,
) {
    AddEditTask(
        route = "OtherDestinations-AddEditTask"
    ),
    TaskDetails(
        route = "OtherDestinations-TaskDetails"
    ),
    SearchTasks(
        route = "OtherDestinations-SearchTasks"
    );
}