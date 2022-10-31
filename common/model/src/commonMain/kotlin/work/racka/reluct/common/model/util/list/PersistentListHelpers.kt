package work.racka.reluct.common.model.util.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

inline fun <T> ImmutableList<T>.filterPersistent(predicate: (T) -> Boolean): ImmutableList<T> {
    return filterTo(persistentListOf<T>().builder(), predicate).build()
}

inline fun <T> ImmutableList<T>.filterPersistentNot(predicate: (T) -> Boolean): ImmutableList<T> {
    val builder = persistentListOf<T>().builder()
    return filterNotTo(builder, predicate).build()
}

/*
inline fun <T, K> ImmutableList<T>.groupByPersistent(keySelector: (T) -> K, valueTrans: (T) -> M ): ImmutableMap<K, ImmutableList<T>> {
    val builder = persistentMapOf<K, ImmutableList<T>>().builder()
    return groupByTo(builder, keySelector).build().toImmutableMap()
}*/
