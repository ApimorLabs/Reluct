package work.racka.reluct.common.data.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.usecases.limits.ManageFocusMode
import work.racka.reluct.common.settings.MultiplatformSettings

internal class ManageFocusModeImpl(
    private val settings: MultiplatformSettings,
    private val backgroundDispatcher: CoroutineDispatcher
) : ManageFocusMode {
    override val isFocusModeOn: Flow<Boolean>
        get() = settings.focusMode

    override val isDoNotDisturbOn: Flow<Boolean>
        get() = settings.focusMode

    override suspend fun toggleFocusMode(isFocusMode: Boolean) {
        withContext(backgroundDispatcher) {
            settings.saveFocusMode(isFocusMode)
        }
    }

    override suspend fun toggleDoNoDisturb(isDnd: Boolean) {
        withContext(backgroundDispatcher) {
            settings.saveDoNotDisturb(isDnd)
        }
    }
}