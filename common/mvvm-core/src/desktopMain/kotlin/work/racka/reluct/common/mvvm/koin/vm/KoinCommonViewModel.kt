package work.racka.reluct.common.mvvm.koin.vm

import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.qualifier.Qualifier
import work.racka.reluct.common.mvvm.viewmodel.CommonViewModel

actual inline fun <reified T : CommonViewModel> org.koin.core.module.Module.commonViewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): Pair<org.koin.core.module.Module, InstanceFactory<T>> {
    return factory(qualifier = qualifier, definition = definition)
}
