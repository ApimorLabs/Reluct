package racka.reluct.common.authentication.managers

import racka.reluct.common.authentication.models.AuthLoginUser
import racka.reluct.common.authentication.models.AuthRegisterUser
import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.util.Resource

internal class DesktopUserAuthentication : UserAuthentication {
    override suspend fun registerWithEmail(user: AuthRegisterUser): Resource<AuthUser> {
        return Resource.Error(message = "Not Implemented")
    }

    override suspend fun loginWithEmail(user: AuthLoginUser): Resource<AuthUser> {
        return Resource.Error(message = "Not Implemented")
    }

    override suspend fun sendVerificationEmail(user: AuthUser) {
        // Not Implemented yet
    }

    override suspend fun checkEmailVerification(): Resource<IsVerified> {
        return Resource.Error(message = "Not Implemented")
    }

    override suspend fun forgotPasswordEmail(email: String): Resource<String> {
        return Resource.Error(message = "Not Implemented")
    }
}
