package work.racka.reluct.compose.common.components.statsHelpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing

actual object StatsDispatcher {
    actual val Dispatcher: CoroutineDispatcher = Dispatchers.Swing
}
