package work.racka.reluct.common.features.screen_time.limits.states

import work.racka.reluct.common.model.domain.app_info.AppInfo

data class ScreenTimeLimitState(
    val focusModeState: FocusModeState = FocusModeState(),
    val pausedAppsState: PausedAppsState = PausedAppsState.Nothing,
    val distractingAppsState: DistractingAppsState = DistractingAppsState.Nothing
)

data class FocusModeState(
    val focusModeOn: Boolean = false,
    val doNotDisturbOn: Boolean = false
)

sealed class PausedAppsState(
    val pausedApps: List<AppInfo>,
    val unPausedApps: List<AppInfo>
) {
    class Data(pausedApps: List<AppInfo>, unPausedApps: List<AppInfo>) : PausedAppsState(
        pausedApps = pausedApps,
        unPausedApps = unPausedApps
    )

    class Loading(
        pausedApps: List<AppInfo> = emptyList(),
        unPausedApps: List<AppInfo> = emptyList()
    ) : PausedAppsState(
        pausedApps = pausedApps,
        unPausedApps = unPausedApps
    )

    object Nothing : PausedAppsState(emptyList(), emptyList())
}

sealed class DistractingAppsState(
    val apps: List<AppInfo>
) {
    class Data(apps: List<AppInfo>) : DistractingAppsState(apps)
    class Loading(apps: List<AppInfo> = emptyList()) : DistractingAppsState(apps)
    object Nothing : DistractingAppsState(emptyList())
}
