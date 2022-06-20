package work.racka.reluct.android.compose.components.util

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Stable
interface BarVisibilityState {
    val isVisible: Boolean
    fun hide()
    fun show()
}

@Parcelize
private class DefaultBarVisibilityState(
    private val defaultVisibility: Boolean = true
) : BarVisibilityState, Parcelable {
    private val _isVisible = mutableStateOf(defaultVisibility)
    override val isVisible: Boolean by _isVisible

    override fun hide() {
        _isVisible.value = false
    }

    override fun show() {
        _isVisible.value = true
    }

    private companion object : Parceler<DefaultBarVisibilityState> {
        override fun create(parcel: Parcel): DefaultBarVisibilityState {
            val isVisibleBool = BooleanArray(1)
            parcel.readBooleanArray(isVisibleBool)
            return DefaultBarVisibilityState(defaultVisibility = isVisibleBool[0])
        }

        override fun DefaultBarVisibilityState.write(parcel: Parcel, flags: Int) {
            parcel.writeBooleanArray(booleanArrayOf(_isVisible.value))
        }
    }
}

@Stable
interface BarsVisibility {
    val topBar: BarVisibilityState
    val bottomBar: BarVisibilityState
    val statusBar: BarVisibilityState
    val navigationBar: BarVisibilityState
}

@Composable
fun rememberBarVisibility(): BarsVisibility {
    val topBarState = rememberSaveable { DefaultBarVisibilityState() }
    val bottomBarState = rememberSaveable { DefaultBarVisibilityState() }
    val statusBarState = rememberSaveable { DefaultBarVisibilityState() }
    val navigationBarState = rememberSaveable { DefaultBarVisibilityState() }

    val barsVisibility: BarsVisibility by remember {
        derivedStateOf {
            object : BarsVisibility {
                override val topBar: BarVisibilityState
                    get() = topBarState
                override val bottomBar: BarVisibilityState
                    get() = bottomBarState
                override val statusBar: BarVisibilityState
                    get() = statusBarState
                override val navigationBar: BarVisibilityState
                    get() = navigationBarState
            }
        }
    }
    return barsVisibility
}