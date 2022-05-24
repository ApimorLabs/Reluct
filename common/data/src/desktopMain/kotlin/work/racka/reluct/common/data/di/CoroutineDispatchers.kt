package work.racka.reluct.common.data.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object CoroutineDispatchers {
    actual val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
}