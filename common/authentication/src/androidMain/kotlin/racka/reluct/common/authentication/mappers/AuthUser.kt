package racka.reluct.common.authentication.mappers

import com.google.firebase.auth.FirebaseUser
import racka.reluct.common.authentication.models.AuthUser

fun FirebaseUser.asAuthUser(): AuthUser = AuthUser(
    id = uid,
    displayName = displayName ?: "User",
    profilePicUrl = photoUrl?.toString(),
    email = email ?: "NO_EMAIL",
    isEmailVerified = isEmailVerified,
)