package work.racka.reluct.common.features.settings.states

import work.racka.reluct.common.model.domain.authentication.User

sealed class UserAccountState {
    data class Account(val user: User) : UserAccountState()
    object NotSignedIn : UserAccountState()
    object Updating : UserAccountState()
}
