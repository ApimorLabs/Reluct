package work.racka.reluct.common.domain.di

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val backgroundDispatcher: CoroutineDispatcher
}
