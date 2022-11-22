package racka.reluct.common.authentication.managers

import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.util.Resource

interface ManageUser {
    fun getAuthUser(): AuthUser?
    fun logOut()
    suspend fun refreshUser(): Resource<AuthUser>
    suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ): Resource<AuthUser>
}
