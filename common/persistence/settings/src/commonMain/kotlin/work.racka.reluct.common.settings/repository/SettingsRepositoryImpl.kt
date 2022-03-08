package work.racka.reluct.common.settings.repository

import com.russhwolf.settings.Settings

internal class SettingsRepositoryImpl(
    private val settings: Settings
) : SettingsRepository {

    private object Keys {
        const val THEME_OPTION = "THEME_OPTION"
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
}