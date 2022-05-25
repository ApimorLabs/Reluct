package work.racka.reluct.common.app.usage.stats.di

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val backgroundDispatcher: CoroutineDispatcher
}