package work.racka.reluct.common.model.domain.authentication

data class User(
    val id: String,
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val isEmailVerified: Boolean
)
