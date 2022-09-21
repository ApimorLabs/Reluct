package racka.reluct.common.authentication.models

data class AuthUser(
    val id: String,
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val isEmailVerified: Boolean = false
)
