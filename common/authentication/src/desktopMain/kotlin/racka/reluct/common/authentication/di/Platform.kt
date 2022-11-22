package racka.reluct.common.authentication.di

import org.koin.core.module.Module
import org.koin.dsl.module
import racka.reluct.common.authentication.managers.DesktopManageUser
import racka.reluct.common.authentication.managers.DesktopUserAuthentication
import racka.reluct.common.authentication.managers.ManageUser
import racka.reluct.common.authentication.managers.UserAuthentication

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<UserAuthentication> {
            DesktopUserAuthentication()
        }
        factory<ManageUser> {
            DesktopManageUser()
        }
    }
}
