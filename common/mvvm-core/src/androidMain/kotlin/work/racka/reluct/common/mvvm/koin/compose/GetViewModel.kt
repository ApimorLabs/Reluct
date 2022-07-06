package work.racka.reluct.common.mvvm.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import work.racka.reluct.common.mvvm.viewmodel.CommonViewModel

/**
 * Resolve ViewModel instance
 *
 * @param qualifier
 * @param parameters
 *
 * @author Arnaud Giuliani
 * @author Racka98
 */
@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : CommonViewModel> getCommonViewModel(
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T {
    return commonViewModel<T>(qualifier, owner, scope, parameters).value
}

@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : CommonViewModel> commonViewModel(
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> {
    val storeOwner = owner?.let { ViewModelOwner.from(it) } ?: getComposeViewModelOwner()
    return remember(qualifier, parameters) {
        ViewModelLazy(T::class, { storeOwner.storeOwner.viewModelStore }, {
            getViewModelFactory<T>({ storeOwner }, qualifier, parameters, scope = scope)
        })
    }
}

/**
 * Retrieve ViewModelOwner for current LocalViewModelStoreOwner & LocalSavedStateRegistryOwner
 *
 * @return ViewModelOwner
 */
@Composable
fun getComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalViewModelStoreOwner.current!!,
        LocalSavedStateRegistryOwner.current
    )
}