package work.racka.reluct.common.settings.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class MultiplatformSettingsImpl(
    private val settings: Settings
) : MultiplatformSettings {

    private object Keys {
        const val THEME_OPTION = "THEME_OPTION"
    }

    private val _themeFlow = MutableSharedFlow<Int>(replay = 1)

    override val theme: SharedFlow<Int>
        get() = _themeFlow.asSharedFlow()

    init {
        initializeSettings()
    }

    private fun initializeSettings() {
        _themeFlow.tryEmit(readThemeSettings())
    }

    override fun saveThemeSettings(value: Int) {
        settings.putInt(
            key = Keys.THEME_OPTION,
            value = value
        )
        _themeFlow.tryEmit(readThemeSettings())
    }

    private fun readThemeSettings(): Int =
        settings.getInt(
            key = Keys.THEME_OPTION,
            defaultValue = -1
        )
}