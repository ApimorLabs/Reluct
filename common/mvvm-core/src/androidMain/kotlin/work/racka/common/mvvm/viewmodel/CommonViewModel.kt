package work.racka.common.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

actual abstract class CommonViewModel actual constructor() : ViewModel() {
    actual val vmScope: CoroutineScope = viewModelScope

    actual open fun destroy() {
        if (vmScope is Closeable) {
            (vmScope as Closeable).close()
        }
    }

    override fun onCleared() {
        super.onCleared()
        destroy()
    }
}
