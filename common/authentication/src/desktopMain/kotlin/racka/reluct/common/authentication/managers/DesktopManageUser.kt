package racka.reluct.common.authentication.managers

import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.util.Resource

internal class DesktopManageUser : ManageUser {
    override fun getAuthUser(): AuthUser? {
        return null
    }

    override fun logOut() {
        // TODO: "Not yet implemented"
    }

    override suspend fun refreshUser(): Resource<AuthUser> {
        return Resource.Error(message = "Not Implemented")
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ): Resource<AuthUser> {
        return Resource.Error(message = "Not Implemented")
    }
}