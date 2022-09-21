package racka.reluct.common.authentication.models

data class AuthRegisterUser(
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val password: String
)
