package racka.reluct.common.authentication.di

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import racka.reluct.common.authentication.managers.AndroidFirebaseUserAuthentication
import racka.reluct.common.authentication.managers.UserAuthentication

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<UserAuthentication> {
            AndroidFirebaseUserAuthentication(
                auth = FirebaseAuth.getInstance(),
                dispatcher = Dispatchers.IO
            )
        }
    }
}