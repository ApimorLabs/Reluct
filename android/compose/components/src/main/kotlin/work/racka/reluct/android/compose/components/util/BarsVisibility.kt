package work.racka.reluct.android.compose.components.util

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

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
private class DefaultBarVisibilityState(
    private val defaultVisibility: Boolean = true
) : BarVisibilityState {
    private val _isVisible = mutableStateOf(defaultVisibility)
    override val isVisible: Boolean by _isVisible

    override fun hide() {
        _isVisible.value = false
    }

    override fun show() {
        _isVisible.value = true
    }

    fun saveState(): Bundle {
        val bundle = Bundle()
        bundle.putBoolean(key, _isVisible.value)
        return bundle
    }

    companion object {
        private const val key = "DefaultBarVisibilityStateKey"
        fun restore(bundle: Bundle): DefaultBarVisibilityState {
            val isVisibleBool = bundle.getBoolean(key)
            return DefaultBarVisibilityState(defaultVisibility = isVisibleBool)
        }
    }
}

private fun defaultBarVisibilityStateSaver(): Saver<DefaultBarVisibilityState, Bundle> {
    return Saver(
        save = { it.saveState() },
        restore = { bundle -> DefaultBarVisibilityState.restore(bundle) }
    )
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

@Stable
private class BarsStates(
    private val topBarState: BarVisibilityState,
    private val bottomBarState: BarVisibilityState,
    private val statusBarState: BarVisibilityState,
    private val navigationBarState: BarVisibilityState,
) : BarsVisibility {
    override val topBar: BarVisibilityState
        get() = topBarState
    override val bottomBar: BarVisibilityState
        get() = bottomBarState
    override val statusBar: BarVisibilityState
        get() = statusBarState
    override val navigationBar: BarVisibilityState
        get() = navigationBarState
}

/**
 * A remember function for [BarsVisibility]. It survives configuration changes
 * It should be used at the top level Composable or where the root NavHost is located
 * and should be passed down to child Composables as needed.
 * [defaultTopBar] sets default visibility for Top Bar when the function is called
 * [defaultBottomBar] sets default visibility for Bottom Bar when the function is called
 */
@Composable
fun rememberBarVisibility(
    defaultTopBar: Boolean = true,
    defaultBottomBar: Boolean = false
): BarsVisibility {
    val topBarState = rememberSaveable(
        saver = defaultBarVisibilityStateSaver()
    ) { DefaultBarVisibilityState(defaultVisibility = defaultTopBar) }
    val bottomBarState = rememberSaveable(
        saver = defaultBarVisibilityStateSaver()
    ) { DefaultBarVisibilityState(defaultVisibility = defaultBottomBar) }
    val statusBarState = rememberSaveable(
        saver = defaultBarVisibilityStateSaver()
    ) { DefaultBarVisibilityState() }
    val navigationBarState = rememberSaveable(
        saver = defaultBarVisibilityStateSaver()
    ) { DefaultBarVisibilityState() }

    return remember {
        BarsStates(
            topBarState = topBarState,
            bottomBarState = bottomBarState,
            statusBarState = statusBarState,
            navigationBarState = navigationBarState
        )
    }
}
