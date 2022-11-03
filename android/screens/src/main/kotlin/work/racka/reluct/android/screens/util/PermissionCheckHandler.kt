package work.racka.reluct.android.screens.util

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun PermissionCheckHandler(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    checkPermission: () -> Unit
) {
    val currentPermissionCheck by rememberUpdatedState(newValue = checkPermission)

    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> currentPermissionCheck()
            Lifecycle.Event.ON_RESUME -> currentPermissionCheck()
            else -> {}
        }
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

fun isAndroid13Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
