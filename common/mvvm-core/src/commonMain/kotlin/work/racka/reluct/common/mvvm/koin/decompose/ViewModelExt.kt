package work.racka.reluct.common.mvvm.koin.decompose

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import work.racka.reluct.common.mvvm.viewmodel.CommonViewModel

/**
 * Resolve CommonViewModel instance
 *
 * @param qualifier
 * @param parameters
 *
 * @author Racka98
 */
@OptIn(KoinInternalApi::class)
inline fun <reified T : CommonViewModel> ComponentContext.getCommonViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T {
    return commonViewModel<T>(qualifier, scope, parameters).value
}

@OptIn(KoinInternalApi::class)
inline fun <reified T : CommonViewModel> ComponentContext.commonViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = DecomposeVMLazy(
    vmProducer = { scope.get(qualifier, parameters) },
    componentContext = this
)
