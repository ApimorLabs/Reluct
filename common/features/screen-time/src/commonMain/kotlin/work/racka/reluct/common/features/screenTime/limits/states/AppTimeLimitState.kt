package work.racka.reluct.common.features.screenTime.limits.states

import work.racka.reluct.common.model.domain.limits.AppTimeLimit

sealed class AppTimeLimitState {
    object Nothing : AppTimeLimitState()
    object Loading : AppTimeLimitState()
    data class Data(val timeLimit: AppTimeLimit) : AppTimeLimitState()
}
