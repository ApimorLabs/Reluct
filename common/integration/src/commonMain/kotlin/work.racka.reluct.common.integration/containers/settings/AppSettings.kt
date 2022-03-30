package work.racka.reluct.common.integration.containers.settings

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.settings.repository.MultiplatformSettings

class AppSettings(
    settings: MultiplatformSettings,
    scope: CoroutineScope
) {
    val host: SettingsContainerHost = SettingsContainerHostImpl(
        settings = settings,
        scope = scope
    )
}