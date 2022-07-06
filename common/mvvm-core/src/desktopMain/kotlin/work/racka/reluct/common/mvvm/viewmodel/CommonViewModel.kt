package work.racka.reluct.common.mvvm.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.Closeable

actual abstract class CommonViewModel actual constructor() {
    actual val vmScope: CoroutineScope
        get() = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    actual open fun destroy() {
        if (vmScope is Closeable) {
            (vmScope as Closeable).close()
        }
    }
}