package work.racka.common.mvvm.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Suppress("UnnecessaryAbstractClass")
actual abstract class CommonViewModel actual constructor() {
    actual val vmScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    actual open fun destroy() {
        vmScope.cancel()
    }
}
