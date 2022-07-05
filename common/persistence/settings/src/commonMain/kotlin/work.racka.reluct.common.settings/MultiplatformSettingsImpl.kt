package work.racka.reluct.common.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class MultiplatformSettingsImpl(
    private val settings: Settings
) : MultiplatformSettings {

    private val _themeFlow = MutableSharedFlow<Int>(replay = 1)
    private val _focusMode = MutableSharedFlow<Boolean>(replay = 1)
    private val _doNotDisturb = MutableSharedFlow<Boolean>(replay = 1)

    override val theme: Flow<Int>
        get() = _themeFlow.asSharedFlow()
    override val focusMode: Flow<Boolean>
        get() = _focusMode.asSharedFlow()
    override val doNoDisturb: Flow<Boolean>
        get() = _doNotDisturb.asSharedFlow()

    init {
        initializeSettings()
    }

    private fun initializeSettings() {
        _themeFlow.tryEmit(readThemeSettings())
        _focusMode.tryEmit(readFocusMode())
        _doNotDisturb.tryEmit(readDoNotDisturb())
    }

    override fun saveThemeSettings(value: Int): Boolean {
        settings.putInt(
            key = Keys.THEME_OPTION,
            value = value
        )
        return _themeFlow.tryEmit(readThemeSettings())
    }

    override fun saveFocusMode(value: Boolean): Boolean {
        settings.putBoolean(
            key = Keys.FOCUS_MODE,
            value = value
        )
        return _focusMode.tryEmit(readFocusMode())
    }

    override fun saveDoNotDisturb(value: Boolean): Boolean {
        settings.putBoolean(
            key = Keys.DO_NOT_DISTURB,
            value = value
        )
        return _doNotDisturb.tryEmit(readDoNotDisturb())
    }

    // Private Read methods
    private fun readThemeSettings(): Int =
        settings.getInt(
            key = Keys.THEME_OPTION,
            defaultValue = -1
        )

    private fun readFocusMode(): Boolean =
        settings.getBoolean(
            key = Keys.FOCUS_MODE,
            defaultValue = false
        )

    private fun readDoNotDisturb(): Boolean =
        settings.getBoolean(
            key = Keys.DO_NOT_DISTURB,
            defaultValue = false
        )
}