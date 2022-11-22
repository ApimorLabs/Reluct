package racka.reluct.common.authentication.managers

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import racka.reluct.common.authentication.extras.Messages
import racka.reluct.common.authentication.mappers.asAuthUser
import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.util.Resource

internal class AndroidFirebaseManageUser(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher
) : ManageUser {

    override fun getAuthUser(): AuthUser? = auth.currentUser?.asAuthUser()

    override fun logOut() = auth.signOut()

    override suspend fun refreshUser(): Resource<AuthUser> = withContext(dispatcher) {
        try {
            auth.currentUser?.reload()?.await()
            getAuthUser()?.let { Resource.Success(it) }
                ?: Resource.Error(message = Messages.NOT_LOGGED_IN)
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: Messages.UNKNOWN_ERROR)
        }
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ): Resource<AuthUser> = withContext(dispatcher) {
        try {
            auth.currentUser?.let { user ->
                val email = user.email
                if (email != null) {
                    val credential = EmailAuthProvider.getCredential(email, oldPassword)
                    user.reauthenticate(credential).await()
                    auth.currentUser?.updatePassword(newPassword)?.await()
                    getAuthUser()?.let { Resource.Success(it) }
                        ?: Resource.Error(Messages.NOT_LOGGED_IN)
                } else {
                    Resource.Error(Messages.EMAIL_INVALID)
                }
            } ?: Resource.Error(Messages.NOT_LOGGED_IN)
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: Messages.UNKNOWN_ERROR)
        }
    }
}
