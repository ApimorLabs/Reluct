package work.racka.common.mvvm.koin.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.ViewModelStoreOwnerProducer
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import work.racka.common.mvvm.viewmodel.CommonViewModel

inline fun <reified T : CommonViewModel> Fragment.getCommonViewModel(
    qualifier: Qualifier? = null,
    noinline owner: ViewModelStoreOwnerProducer = { this },
    noinline parameters: ParametersDefinition? = null
): T = commonViewModel<T>(qualifier, owner, parameters).value

@OptIn(KoinInternalApi::class)
inline fun <reified T : CommonViewModel> Fragment.commonViewModel(
    qualifier: Qualifier? = null,
    noinline owner: ViewModelStoreOwnerProducer = { this },
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = viewModels(ownerProducer = owner) {
    getViewModelFactory<T>(owner(), qualifier, parameters, scope = getKoinScope())
}
