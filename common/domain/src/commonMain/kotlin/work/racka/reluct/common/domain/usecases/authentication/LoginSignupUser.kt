package work.racka.reluct.common.domain.usecases.authentication

import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.RegisterUser
import work.racka.reluct.common.model.domain.authentication.User
import work.racka.reluct.common.model.util.Resource

interface LoginSignupUser {
    suspend fun emailLogin(login: EmailUserLogin): Resource<User>
    suspend fun emailSignup(register: RegisterUser): Resource<User>
    suspend fun checkEmailVerification(): Boolean
    suspend fun sendVerificationEmail(user: User)
}
