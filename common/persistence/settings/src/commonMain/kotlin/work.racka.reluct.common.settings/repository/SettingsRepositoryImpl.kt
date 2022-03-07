package work.racka.thinkrchive.v2.common.settings.repository

import com.russhwolf.settings.Settings

internal class SettingsRepositoryImpl(
    private val settings: Settings
) : SettingsRepository {

    private object Keys {
        const val THEME_OPTION = "THEME_OPTION"
        const val SORT_OPTION = "SORT_OPTION"
    }

    override fun saveThemeSettings(value: Int) {
        settings.putInt(
            key = Keys.THEME_OPTION,
            value = value
        )
    }

    override fun readThemeSettings(): Int =
        settings.getInt(
            key = Keys.THEME_OPTION,
            defaultValue = -1
        )

    override fun saveSortSettings(value: Int) {
        settings.putInt(
            key = Keys.SORT_OPTION,
            value = value
        )
    }

    override fun readSortSettings(): Int =
        settings.getInt(
            key = Keys.SORT_OPTION,
            defaultValue = 0
        )
}