package work.racka.common.mvvm.koin.vm

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ViewModelOwnerDefinition
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import work.racka.common.mvvm.viewmodel.CommonViewModel

@OptIn(KoinInternalApi::class)
inline fun <reified T : CommonViewModel> ComponentActivity.getCommonViewModel(
    qualifier: Qualifier? = null,
    noinline owner: ViewModelOwnerDefinition = {
        ViewModelOwner.from(
            this as ViewModelStoreOwner,
            this as? SavedStateRegistryOwner
        )
    },
    noinline parameters: ParametersDefinition? = null
): T = commonViewModel<T>(qualifier, owner, parameters).value

@OptIn(KoinInternalApi::class)
inline fun <reified T : CommonViewModel> ComponentActivity.commonViewModel(
    qualifier: Qualifier? = null,
    noinline owner: ViewModelOwnerDefinition = {
        ViewModelOwner.from(
            this as ViewModelStoreOwner,
            this as? SavedStateRegistryOwner
        )
    },
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    val scope = getKoinScope()
    return ViewModelLazy(
        T::class,
        { viewModelStore },
        { getViewModelFactory<T>(owner, qualifier, parameters, scope = scope) })
}