package work.racka.common.mvvm.koin.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.isActive
import work.racka.common.mvvm.viewmodel.CommonViewModel

class DecomposeVMLazy<VM : CommonViewModel>(
    private val vmProducer: () -> VM,
    componentContext: ComponentContext
) : Lazy<VM> {

    var componentInitialized = false
    override val value: VM
        get() = vmProducer()

    override fun isInitialized(): Boolean = componentInitialized

    init {
        with(componentContext.lifecycle) {
            doOnCreate {
                componentInitialized = true
            }

            doOnDestroy {
                value.destroy()
                println("Is scope Active: ${value.vmScope.coroutineContext.isActive}")
            }
        }
    }
}