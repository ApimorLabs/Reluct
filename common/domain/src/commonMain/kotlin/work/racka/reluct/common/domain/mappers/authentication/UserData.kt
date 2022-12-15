package work.racka.reluct.common.domain.mappers.authentication

import racka.reluct.common.authentication.models.AuthLoginUser
import racka.reluct.common.authentication.models.AuthRegisterUser
import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.RegisterUser
import work.racka.reluct.common.model.domain.authentication.User

fun AuthUser.toUser() = User(
    id = id,
    displayName = displayName,
    profilePicUrl = profilePicUrl,
    email = email,
    isEmailVerified = isEmailVerified
)

fun User.toAuthUser() = AuthUser(
    id = id,
    displayName = displayName,
    profilePicUrl = profilePicUrl,
    email = email,
    isEmailVerified = isEmailVerified
)

fun RegisterUser.toAuthRegisterUser() = AuthRegisterUser(
    displayName = displayName,
    profilePicUrl = profilePicUrl,
    email = email,
    password = password
)

fun EmailUserLogin.toAuthLoginUser() = AuthLoginUser(
    email = email,
    password = password
)
