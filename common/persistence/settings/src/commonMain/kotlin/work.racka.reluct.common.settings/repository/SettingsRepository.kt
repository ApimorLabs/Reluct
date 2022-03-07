package work.racka.thinkrchive.v2.common.settings.repository

interface SettingsRepository {
    fun saveThemeSettings(value: Int)
    fun readThemeSettings(): Int
    fun saveSortSettings(value: Int)
    fun readSortSettings(): Int
}