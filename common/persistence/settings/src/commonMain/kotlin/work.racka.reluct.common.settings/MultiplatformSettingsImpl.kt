package work.racka.reluct.common.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class MultiplatformSettingsImpl(
    private val settings: Settings
) : MultiplatformSettings {

    private val _themeFlow = MutableSharedFlow<Int>(replay = 1)

    override val theme: Flow<Int>
        get() = _themeFlow.asSharedFlow()

    init {
        initializeSettings()
    }

    private fun initializeSettings() {
        _themeFlow.tryEmit(readThemeSettings())
    }

    override fun saveThemeSettings(value: Int): Boolean {
        settings.putInt(
            key = Keys.THEME_OPTION,
            value = value
        )
        return _themeFlow.tryEmit(readThemeSettings())
    }

    private fun readThemeSettings(): Int =
        settings.getInt(
            key = Keys.THEME_OPTION,
            defaultValue = -1
        )
}