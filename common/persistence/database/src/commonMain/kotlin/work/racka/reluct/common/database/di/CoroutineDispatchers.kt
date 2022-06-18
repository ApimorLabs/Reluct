package work.racka.reluct.common.database.di

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val backgroundDispatcher: CoroutineDispatcher
}