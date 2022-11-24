package work.racka.reluct

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
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
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.TimeZone
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.resources.stringResource
import kotlin.jvm.optionals.getOrNull

class ProcessesProvider {

    init {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            flow<Int> {
                var run = 0
                while (run < 10) {
                    WinSysUser32HWND.run()
                    emit(run)
                    delay(2000L)
                    run++
                }
            }.collectLatest { }
        }.invokeOnCompletion {
            scope.cancel()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getProcesses(): List<ProcessHandle> = ProcessHandle.allProcesses()
        .filter { it.parent().isPresent }
        .map { it.parent().getOrNull() }
        .toList()
        .mapNotNull { it }
        .distinctBy { it.info().command().getOrNull() }
}

object WinSysUser32HWND {
    @JvmStatic
    fun run() {
        val buffer = CharArray(1024 * 2)
        val lpdw = IntByReference(2)
        val user32 = User32.INSTANCE
        user32.GetWindowTextW(user32.GetForegroundWindow(), buffer, 1024)
        val processId = user32.GetWindowThreadProcessId(user32.GetForegroundWindow(), lpdw)
        println("Active window title: " + Native.toString(buffer))
        println("Thread ID: $processId")
    }

    interface User32 : StdCallLibrary {
        fun GetForegroundWindow(): WinDef.HWND?
        fun GetWindowThreadProcessId(hWnd: WinDef.HWND?, lpdwProcessId: IntByReference?): Int
        fun GetWindowTextW(hWnd: WinDef.HWND?, lpString: CharArray?, nMaxCount: Int)

        companion object {
            val INSTANCE = Native.load(
                "user32",
                User32::class.java
            ) as User32
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
fun TestScreen(modifier: Modifier = Modifier) {
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

    Column(modifier = modifier) {
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            modifier = Modifier
                .onPreviewKeyEvent {
                    when {
                        (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown)
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
