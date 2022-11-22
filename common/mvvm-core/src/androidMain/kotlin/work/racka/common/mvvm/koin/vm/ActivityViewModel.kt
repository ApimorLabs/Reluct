package work.racka.common.mvvm.koin.vm

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import work.racka.common.mvvm.viewmodel.CommonViewModel

inline fun <reified T : CommonViewModel> ComponentActivity.getCommonViewModel(
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner = this,
    noinline parameters: ParametersDefinition? = null
): T = commonViewModel<T>(qualifier, owner, parameters).value

@OptIn(KoinInternalApi::class)
inline fun <reified T : CommonViewModel> ComponentActivity.commonViewModel(
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner = this,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = viewModels {
    getViewModelFactory<T>(owner, qualifier, parameters, scope = getKoinScope())
}
