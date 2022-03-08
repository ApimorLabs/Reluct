package work.racka.reluct.common.model.states.about

import domain.about.AppAbout

sealed class AboutState {
    data class State(
        val appAbout: AppAbout = AppAbout(),
        val hasUpdates: Boolean = false
    ) : AboutState()

    companion object {
        val EmptyState = State()
    }
}