package work.racka.reluct.common.features.screen_time.states

sealed class ScreenTimeStatsEvents {
    object Nothing : ScreenTimeStatsEvents()

    sealed class Navigation : ScreenTimeStatsEvents() {
        data class NavigateToAppInfo(
            val packageName: String
        ) : Navigation()

        data class OpenAppTimerSettings(
            val packageName: String
        ) : Navigation()

        object GoBack : Navigation()
    }
}
