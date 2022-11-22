package work.racka.reluct.common.billing.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.billing.api.BillingApi
import work.racka.reluct.common.billing.api.DesktopBilling

internal actual object Platform {
    actual fun module(): Module = module {
        single<BillingApi> { DesktopBilling() }
    }
}
