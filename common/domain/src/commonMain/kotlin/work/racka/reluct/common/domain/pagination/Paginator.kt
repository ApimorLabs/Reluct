package work.racka.reluct.common.domain.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}