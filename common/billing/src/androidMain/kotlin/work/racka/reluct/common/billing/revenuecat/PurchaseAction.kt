package work.racka.reluct.common.billing.revenuecat

import android.app.Activity
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.purchasePackageWith

class PurchaseAction(
    private val onSuccess: () -> Unit,
    private val onError: (errorMsg: String, userCancelled: Boolean) -> Unit
) {
    fun initiate(activity: Activity, item: Package) {
        Purchases.sharedInstance.purchasePackageWith(
            activity = activity,
            packageToPurchase = item,
            onSuccess = { _, _ ->
                onSuccess()
            },
            onError = { error, userCancelled ->
                onError(error.message, userCancelled)
            }
        )
    }
}
