package work.racka.reluct.common.settings.repository

interface SettingsRepository {
    fun saveThemeSettings(value: Int)
    fun readThemeSettings(): Int
}