package work.racka.common.mvvm.viewmodel

import kotlinx.coroutines.*

@Suppress("UnnecessaryAbstractClass")
actual abstract class CommonViewModel actual constructor() {
    actual val vmScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    actual open fun destroy() {
        vmScope.coroutineContext.cancel()
        vmScope.cancel()
    }
}
