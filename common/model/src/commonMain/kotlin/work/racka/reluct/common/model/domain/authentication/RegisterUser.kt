package work.racka.reluct.common.model.domain.authentication

data class RegisterUser(
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val password: String,
    val repeatPassword: String
)
