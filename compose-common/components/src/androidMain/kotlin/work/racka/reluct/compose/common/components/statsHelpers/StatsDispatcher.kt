package work.racka.reluct.compose.common.components.statsHelpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object StatsDispatcher {
    actual val Dispatcher: CoroutineDispatcher = Dispatchers.Main
}
