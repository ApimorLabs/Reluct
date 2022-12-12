package work.racka.reluct.common.domain.usecases.authentication.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import racka.reluct.common.authentication.managers.ManageUser
import racka.reluct.common.authentication.managers.UserAuthentication
import work.racka.reluct.common.domain.mappers.authentication.toUser
import work.racka.reluct.common.domain.usecases.authentication.UserAccountManagement
import work.racka.reluct.common.model.domain.authentication.User
import work.racka.reluct.common.model.util.Resource

internal class UserAccountManagementImpl(
    private val manageUser: ManageUser,
    private val userAuth: UserAuthentication,
    private val dispatcher: CoroutineDispatcher
) : UserAccountManagement {
    override fun getUser(): User? = manageUser.getAuthUser()?.toUser()

    override fun logout() {
        manageUser.logOut()
    }

    override suspend fun refreshUser(): Resource<User> = withContext(dispatcher) {
        when (val res = manageUser.refreshUser()) {
            is Resource.Success -> Resource.Success(res.data.toUser())
            is Resource.Error -> Resource.Error(message = res.message)
            else -> Resource.Error(message = "Unknown Error!")
        }
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): Resource<User> =
        withContext(dispatcher) {
            when (val res =
                manageUser.updatePassword(oldPassword = oldPassword, newPassword = newPassword)) {
                is Resource.Success -> Resource.Success(res.data.toUser())
                is Resource.Error -> Resource.Error(message = res.message)
                else -> Resource.Error(message = "Unknown Error!")
            }
        }

    override suspend fun requestPasswordResetEmail(email: String): Resource<String> =
        withContext(dispatcher) {
            when (val res = userAuth.forgotPasswordEmail(email)) {
                is Resource.Success -> Resource.Success(res.data)
                is Resource.Error -> Resource.Error(message = res.message)
                else -> Resource.Error(message = "Unknown Error!")
            }
        }
}
