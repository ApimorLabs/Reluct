package work.racka.reluct.common.features.screen_time.statistics.states

import work.racka.reluct.common.model.domain.limits.AppTimeLimit

sealed class ScreenTimeStatsEvents {
    object Nothing : ScreenTimeStatsEvents()
    data class ShowMessageDone(val isDone: Boolean, val msg: String) : ScreenTimeStatsEvents()
    class TimeLimitChange(val app: AppTimeLimit) : ScreenTimeStatsEvents()

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
