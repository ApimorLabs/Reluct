package work.racka.common.mvvm.koin.vm

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.qualifier.Qualifier
import work.racka.common.mvvm.viewmodel.CommonViewModel

actual inline fun <reified T : CommonViewModel> org.koin.core.module.Module.commonViewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): Pair<org.koin.core.module.Module, InstanceFactory<T>> {
    return viewModel(qualifier = qualifier, definition = definition)
}
