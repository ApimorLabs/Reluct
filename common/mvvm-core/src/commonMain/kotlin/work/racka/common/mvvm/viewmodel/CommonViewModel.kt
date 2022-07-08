package work.racka.common.mvvm.viewmodel

import kotlinx.coroutines.CoroutineScope

expect abstract class CommonViewModel() {
    val vmScope: CoroutineScope
    open fun destroy()
}