package work.racka.reluct.common.data.pagination

interface Paginatior<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}