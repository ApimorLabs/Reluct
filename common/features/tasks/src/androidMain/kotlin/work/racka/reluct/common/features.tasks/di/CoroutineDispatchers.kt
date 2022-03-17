package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object CoroutineDispatchers {
    actual val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
}