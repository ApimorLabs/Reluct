package work.racka.reluct.common.mvvm.koin.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import work.racka.reluct.common.mvvm.viewmodel.CommonViewModel

class DecomposeVMLazy<VM : CommonViewModel>(
    private val vmProducer: () -> VM,
    private val componentContext: ComponentContext
) : ComponentContext by componentContext, Lazy<VM> {

    override val value: VM
        get() = vmProducer()

    override fun isInitialized(): Boolean = true

    init {
        lifecycle.doOnDestroy {
            value.destroy()
        }
    }
}