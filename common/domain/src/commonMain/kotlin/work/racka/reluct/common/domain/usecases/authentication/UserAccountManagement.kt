package work.racka.reluct.common.domain.usecases.authentication

import work.racka.reluct.common.model.domain.authentication.User
import work.racka.reluct.common.model.util.Resource

interface UserAccountManagement {
    fun getUser(): User?
    fun logout()
    suspend fun refreshUser(): Resource<User>
    suspend fun updatePassword(oldPassword: String, newPassword: String): Resource<User>

    /**
     * Returns the [email] on success
     */
    suspend fun requestPasswordResetEmail(email: String): Resource<String>
}
