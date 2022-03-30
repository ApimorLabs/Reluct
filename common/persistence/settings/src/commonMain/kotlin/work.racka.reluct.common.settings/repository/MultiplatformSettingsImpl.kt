package work.racka.reluct.common.settings.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class MultiplatformSettingsImpl(
    private val settings: Settings
) : MultiplatformSettings {

    private object Keys {
        const val THEME_OPTION = "THEME_OPTION"
    }

    private val _themeFlow = MutableStateFlow(readThemeSettings())

    override val theme: Flow<Int>
        get() = _themeFlow

    override fun saveThemeSettings(value: Int) {
        settings.putInt(
            key = Keys.THEME_OPTION,
            value = value
        )
        _themeFlow.update { readThemeSettings() }
    }

    private fun readThemeSettings(): Int =
        settings.getInt(
            key = Keys.THEME_OPTION,
            defaultValue = -1
        )
}