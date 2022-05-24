package work.racka.reluct.common.data.di

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val backgroundDispatcher: CoroutineDispatcher
}