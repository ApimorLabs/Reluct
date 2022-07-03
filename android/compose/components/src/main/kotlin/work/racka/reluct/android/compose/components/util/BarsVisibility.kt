package work.racka.reluct.android.compose.components.util

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

/**
 * The base properties and methods that each item will need.
 * Just to make it easier to add more bars item that can need hiding.
 * Eg. scroll bars, navigation rails, etc.
 */
@Stable
interface BarVisibilityState {
    val isVisible: Boolean
    fun hide()
    fun show()
}

/**
 * The default bar visibility state implementation.
 * The class is marked with @Parcelize to make it easier to save it into a bundle so that it
 * survives configuration changes. This uses kotlin-parcelize.
 * Without parceling this class you would have to implement your own Saver to be used with
 * rememberSaveable{ }.
 * You could also use kotlinx-serialize (@Serializable) for using in Compose Multiplatform or
 * just make your own Saver.
 */
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

/**
 * The bars you want to change their visibility for.
 * The items are of the interface [BarVisibilityState] for uniformity.
 */
@Stable
interface BarsVisibility {
    val topBar: BarVisibilityState
    val bottomBar: BarVisibilityState

    // StatusBar and NavBar items are useful for triggering immersive mode
    val statusBar: BarVisibilityState
    val navigationBar: BarVisibilityState
}

/**
 * A remember function for [BarsVisibility]. It survives configuration changes
 * It should be used at the top level Composable or where the root NavHost is located
 * and should be passed down to child Composables as needed.
 */
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