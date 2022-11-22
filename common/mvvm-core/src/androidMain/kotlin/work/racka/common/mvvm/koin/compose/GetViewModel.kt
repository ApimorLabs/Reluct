package work.racka.common.mvvm.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import work.racka.common.mvvm.viewmodel.CommonViewModel

/**
 * Resolve CommonViewModel instance
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
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T {
    return remember(qualifier, parameters) {
        val vmClazz = T::class
        val factory = getViewModelFactory(
            owner,
            vmClazz,
            qualifier,
            parameters,
            scope = scope
        )
        ViewModelProvider(owner, factory)[vmClazz.java]
    }
}

/*
@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : CommonViewModel> commonViewModel(
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> {
    val storeOwner = owner ?: getComposeViewModelOwner()
    return remember(qualifier, parameters) {
        ViewModelLazy(T::class, { storeOwner.viewModelStore }, {
            getViewModelFactory<T>({ storeOwner.viewModelStore }, qualifier, parameters, scope = scope)
        })
    }
}

*/
/**
 * Retrieve ViewModelOwner for current LocalViewModelStoreOwner & LocalSavedStateRegistryOwner
 *
 * @return ViewModelOwner
 *//*

@Composable
fun getComposeViewModelOwner(): ViewModelStoreOwner {
    ViewModelStoreOwner { ViewModelStore() }
    return LocalViewModelStoreOwner.current!!
}*/
