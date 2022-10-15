package work.racka.reluct.common.data.usecases.limits

import kotlinx.coroutines.flow.Flow

interface ManageFocusMode {
    val isFocusModeOn: Flow<Boolean>
    val isDoNotDisturbOn: Flow<Boolean>
    val isAppBlockingEnabled: Flow<Boolean>

    suspend fun toggleFocusMode(isFocusMode: Boolean)
    suspend fun toggleDoNoDisturb(isDnd: Boolean)
}