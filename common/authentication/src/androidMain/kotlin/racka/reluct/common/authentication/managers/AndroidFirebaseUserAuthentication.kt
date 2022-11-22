package racka.reluct.common.authentication.managers

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import racka.reluct.common.authentication.extras.Messages
import racka.reluct.common.authentication.mappers.asAuthUser
import racka.reluct.common.authentication.models.AuthLoginUser
import racka.reluct.common.authentication.models.AuthRegisterUser
import racka.reluct.common.authentication.models.AuthUser
import work.racka.reluct.common.model.util.Resource

internal class AndroidFirebaseUserAuthentication(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher
) : UserAuthentication {

    override suspend fun registerWithEmail(user: AuthRegisterUser): Resource<AuthUser> =
        withContext(dispatcher) {
            if (auth.currentUser == null) {
                try {
                    val createRes = auth.createUserWithEmailAndPassword(user.email, user.password)
                        .await()
                    createRes.user?.let { fbUser ->
                        val updateUserRequest = userProfileChangeRequest {
                            user.profilePicUrl?.let { photoUri = it.toUri() }
                            displayName = user.displayName
                        }
                        fbUser.updateProfile(updateUserRequest).await()
                    }
                    auth.currentUser?.let {
                        Resource.Success(it.asAuthUser())
                    } ?: Resource.Error(message = Messages.NOT_LOGGED_IN)
                } catch (e: Exception) {
                    Resource.Error(message = e.localizedMessage ?: Messages.UNKNOWN_ERROR)
                }
            } else {
                Resource.Error(message = Messages.LOGGED_IN)
            }
        }

    override suspend fun loginWithEmail(user: AuthLoginUser): Resource<AuthUser> =
        withContext(dispatcher) {
            try {
                val loginRes = auth.signInWithEmailAndPassword(user.email, user.password).await()
                loginRes.user?.let { Resource.Success(it.asAuthUser()) }
                    ?: Resource.Error(message = Messages.NOT_LOGGED_IN)
            } catch (e: Exception) {
                Resource.Error(message = e.localizedMessage ?: Messages.UNKNOWN_ERROR)
            }
        }

    override suspend fun sendVerificationEmail(user: AuthUser) {
        withContext(dispatcher) {
            try {
                auth.currentUser?.sendEmailVerification()?.await()
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage ?: Messages.UNKNOWN_ERROR)
            }
        }
    }

    override suspend fun checkEmailVerification(): Resource<IsVerified> = withContext(dispatcher) {
        try {
            auth.currentUser?.reload()?.await()
            auth.currentUser?.let { user ->
                Resource.Success(user.isEmailVerified)
            } ?: Resource.Error(message = Messages.NOT_LOGGED_IN)
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: Messages.UNKNOWN_ERROR)
        }
    }

    override suspend fun forgotPasswordEmail(email: String): Resource<String> =
        withContext(dispatcher) {
            try {
                auth.sendPasswordResetEmail(email).await()
                Resource.Success(email)
            } catch (e: Exception) {
                println(e)
                Resource.Error(message = Messages.EMAIL_SEND_FAILED)
            }
        }

    companion object {
        private const val TAG = "FirebaseUserAuthentication"
    }
}
