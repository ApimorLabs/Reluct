package work.racka.reluct.common.data.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}