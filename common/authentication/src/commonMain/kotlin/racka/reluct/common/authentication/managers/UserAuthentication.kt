package racka.reluct.common.authentication.managers

import racka.reluct.common.authentication.models.AuthLoginUser
import racka.reluct.common.authentication.models.AuthRegisterUser
import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.util.Resource

typealias IsVerified = Boolean

interface UserAuthentication {
    suspend fun registerWithEmail(user: AuthRegisterUser): Resource<AuthUser>
    suspend fun loginWithEmail(user: AuthLoginUser): Resource<AuthUser>
    suspend fun sendVerificationEmail(user: AuthUser)
    suspend fun checkEmailVerification(): Resource<IsVerified>
}