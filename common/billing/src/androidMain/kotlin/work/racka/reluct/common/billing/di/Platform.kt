package work.racka.reluct.common.billing.di

import com.revenuecat.purchases.Purchases
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.billing.api.BillingApi
import work.racka.reluct.common.billing.api.RevenueCatAndroid

internal actual object Platform {
    actual fun module(): Module = module {
        single<BillingApi> {
            RevenueCatAndroid(
                purchases = Purchases.sharedInstance,
                dispatcher = Dispatchers.IO
            )
        }
    }
}
