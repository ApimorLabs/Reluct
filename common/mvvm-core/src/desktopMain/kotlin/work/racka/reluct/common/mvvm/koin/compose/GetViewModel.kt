package work.racka.reluct.common.mvvm.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import work.racka.reluct.common.mvvm.viewmodel.CommonViewModel

/**
 * Resolve a dependency for [Composable] functions
 * @param qualifier
 * @param parameters
 *
 * @return Lazy instance of type T
 *
 * @author Arnaud Giuliani
 * @author Henrique Horbovyi
 * @author Racka98
 */
@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : CommonViewModel> getCommonViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T = remember(qualifier, parameters) {
    scope.get(qualifier, parameters)
}

@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : CommonViewModel> commonViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = remember(qualifier, parameters) {
    scope.inject(qualifier, LazyThreadSafetyMode.NONE, parameters)
}