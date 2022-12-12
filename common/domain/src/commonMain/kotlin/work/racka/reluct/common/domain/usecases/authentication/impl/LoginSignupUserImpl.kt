package work.racka.reluct.common.domain.usecases.authentication.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import racka.reluct.common.authentication.managers.UserAuthentication
import work.racka.reluct.common.domain.mappers.authentication.toAuthLoginUser
import work.racka.reluct.common.domain.mappers.authentication.toAuthRegisterUser
import work.racka.reluct.common.domain.mappers.authentication.toAuthUser
import work.racka.reluct.common.domain.mappers.authentication.toUser
import work.racka.reluct.common.domain.usecases.authentication.LoginSignupUser
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.RegisterUser
import work.racka.reluct.common.model.domain.authentication.User
import work.racka.reluct.common.model.util.Resource

internal class LoginSignupUserImpl(
    private val userAuth: UserAuthentication,
    private val dispatcher: CoroutineDispatcher
) : LoginSignupUser {
    override suspend fun emailLogin(login: EmailUserLogin): Resource<User> =
        withContext(dispatcher) {
            userAuth.loginWithEmail(login.toAuthLoginUser()).let {
                when (val res = it) {
                    is Resource.Success -> Resource.Success(res.data.toUser())
                    is Resource.Error -> Resource.Error(message = res.message)
                    else -> Resource.Error(message = "Unknown Error!")
                }
            }
        }

    override suspend fun emailSignup(register: RegisterUser): Resource<User> =
        withContext(dispatcher) {
            userAuth.registerWithEmail(register.toAuthRegisterUser()).let {
                when (val res = it) {
                    is Resource.Success -> Resource.Success(res.data.toUser())
                    is Resource.Error -> Resource.Error(message = res.message)
                    else -> Resource.Error(message = "Unknown Error!")
                }
            }
            TODO()
        }

    override suspend fun checkEmailVerification(): Boolean = withContext(dispatcher) {
        when (val res = userAuth.checkEmailVerification()) {
            is Resource.Success -> res.data
            else -> false
        }
    }

    override suspend fun sendVerificationEmail(user: User) = withContext(dispatcher) {
        userAuth.sendVerificationEmail(user = user.toAuthUser())
    }
}
