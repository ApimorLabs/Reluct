package work.racka.reluct

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.datetime.TimeZone
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.resources.stringResource
import kotlin.jvm.optionals.getOrNull

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(SharedRes.strings.app_name),
        icon = androidx.compose.ui.res.painterResource("icons/window_icon.svg")
    ) {
        MaterialTheme {
            ProcessesScreen()
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalStdlibApi::class)
@Composable
private fun ProcessesScreen() {
    val provider = remember { ProcessesProvider() }
    val processes = remember { derivedStateOf { provider.getProcesses() } }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            Text(text = "Processes")
        }

        item {
            Image(
                painter = painterResource(resource = SharedRes.assets.welcome_mobile),
                contentDescription = "Test Image",
                modifier = Modifier.size(160.dp)
            )
        }

        item {
            Text(
                text = stringResource(
                    resource = SharedRes.strings.task_info_reminder_text,
                    "Hello"
                )
            )
        }

        item {
            Button(
                onClick = {
                    WinSysUser32HWND.run()
                }
            ) {
                Text(text = "Get Active Window")
            }
        }

        items(processes.value) { item ->
            Text(text = "PID: ${item.pid()}")
            Text(text = "Is Alive: ${item.isAlive}")
            Text(text = "Command: ${item.info().command().getOrNull()}")
            Text(text = "User: ${item.info().user()}")
            Text(text = "CPU Time: ${item.info().totalCpuDuration().get().seconds}")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TestScreen() {
    val text = remember {
        mutableStateOf("2010-06-01T22:08:01")
    }
    val ans = remember { mutableStateOf("Nothing") }
    fun click() {
        ans.value = TimeUtils
            .getFormattedDateString(
                dateTime = text.value,
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
    }

    Column {
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            modifier = Modifier
                .onPreviewKeyEvent {
                    when {
                        (
                                it.isCtrlPressed && it.key == Key.Enter &&
                                        it.type == KeyEventType.KeyDown
                                )
                        -> {
                            click()
                            true
                        }
                        else -> false
                    }
                }
        )
        Spacer(Modifier.height(8.dp))
        Text(ans.value)
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                click()
            }
        ) {
            Text("Compute")
        }
    }
}
