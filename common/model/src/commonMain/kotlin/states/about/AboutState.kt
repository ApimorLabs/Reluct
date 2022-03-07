package states.about

import domain.AppAbout

sealed class AboutState {
    data class State(
        val appAbout: AppAbout = AppAbout(),
        val hasUpdates: Boolean = false
    ) : AboutState()

    companion object {
        val EmptyState = State()
    }
}